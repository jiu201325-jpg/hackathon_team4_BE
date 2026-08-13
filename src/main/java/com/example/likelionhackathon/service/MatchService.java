package com.example.likelionhackathon.service;

import com.example.likelionhackathon.dto.*;
import com.example.likelionhackathon.entity.Ingredient;
import com.example.likelionhackathon.entity.KoreanMedication;
import com.example.likelionhackathon.entity.USMedication;
import com.example.likelionhackathon.repository.KoreanMedicationRepository;
import com.example.likelionhackathon.repository.USMedicationRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final KoreanMedicationRepository koreanMedicationRepository;
    private final USMedicationRepository usMedicationRepository;

    public MatchService(KoreanMedicationRepository koreanMedicationRepository,
                        USMedicationRepository usMedicationRepository) {
        this.koreanMedicationRepository = koreanMedicationRepository;
        this.usMedicationRepository = usMedicationRepository;
    }

    public MatchResponse compare(MatchRequest request) {
        List<KoreanMedication> koreanMeds = koreanMedicationRepository.findAllById(request.getKoreanMedicationIds());

        Set<Ingredient> koreanIngredients = koreanMeds.stream()
                .flatMap(m -> m.getIngredients().stream())
                .collect(Collectors.toSet());

        List<USMedication> candidates = usMedicationRepository.findByCategories_Id(request.getSymptomCategoryId());

        List<MatchResult> results = new ArrayList<>();
        for (USMedication usMed : candidates) {
            results.add(evaluate(usMed, koreanIngredients));
        }

        MatchResponse response = new MatchResponse();
        response.setResults(results);
        return response;
    }

    private MatchResult evaluate(USMedication usMed, Set<Ingredient> koreanIngredients) {
        List<Ingredient> usIngredients = usMed.getIngredients();

        // 확인불가: 성분 정보 자체가 없음
        if (usIngredients == null || usIngredients.isEmpty()) {
            return build(usMed, "확인불가", List.of(),
                    "성분 정보가 확인되지 않아 판단이 어렵습니다. 약사에게 확인하세요.");
        }

        // 1단계: 정확히 같은 성분 (standardCode 완전 일치) → 겹침
        List<Ingredient> exactMatches = usIngredients.stream()
                .filter(usIng -> usIng.getStandardCode() != null && koreanIngredients.stream()
                        .anyMatch(krIng -> usIng.getStandardCode().equals(krIng.getStandardCode())))
                .toList();

        if (!exactMatches.isEmpty()) {
            return build(usMed, "겹침", exactMatches,
                    "복용 중인 약과 동일한 활성 성분이 포함되어 있습니다.");
        }

        // 2단계: 같은 계열(ATC) → 추가확인
        List<Ingredient> classMatches = usIngredients.stream()
                .filter(usIng -> usIng.getAtcClass() != null && koreanIngredients.stream()
                        .anyMatch(krIng -> sameAtcClass(usIng.getAtcClass(), krIng.getAtcClass())))
                .toList();

        if (!classMatches.isEmpty()) {
            return build(usMed, "추가확인", classMatches,
                    "같은 계열의 성분이 포함되어 있어 약사에게 추가 확인이 필요합니다.");
        }

        // 3단계: 성분 데이터 자체가 불완전한 경우 → 확인불가
        boolean incompleteData = usIngredients.stream()
                .anyMatch(i -> i.getStandardCode() == null && i.getAtcClass() == null);
        if (incompleteData) {
            return build(usMed, "확인불가", List.of(),
                    "일부 성분 정보가 확인되지 않아 정확한 비교가 어렵습니다. 약사에게 확인하세요.");
        }

        // 4단계: 그 외 → 겹침없음
        return build(usMed, "겹침없음", List.of(),
                "등록된 약과 같은 핵심 성분은 찾지 못했습니다. 전체 안전을 뜻하지는 않으니 약사에게 확인하세요.");
    }

    // ATC 코드 앞 5자리(화학적 소분류 수준)까지 같으면 같은 계열로 판단
    private boolean sameAtcClass(String atc1, String atc2) {
        if (atc1 == null || atc2 == null) return false;
        int len = Math.min(atc1.length(), atc2.length());
        int compareLen = Math.min(len, 5);
        return atc1.substring(0, compareLen).equals(atc2.substring(0, compareLen));
    }

    private MatchResult build(USMedication usMed, String status, List<Ingredient> matched, String reason) {
        MatchResult result = new MatchResult();

        USMedicationDto dto = new USMedicationDto();
        dto.setId(usMed.getId());
        dto.setName(usMed.getName());
        dto.setManufacturer(usMed.getManufacturer());
        dto.setImageUrl(usMed.getImageUrl());
        dto.setEfficacyText(usMed.getEfficacyText());
        result.setUsMedication(dto);

        result.setMatchStatus(status);
        result.setMatchedIngredients(matched.stream()
                .map(i -> new IngredientDto(i.getId(), i.getNameEn()))
                .toList());
        result.setReason(reason);
        result.setSource(usMed.getSource());
        result.setLastVerifiedAt(usMed.getLastVerifiedAt());

        return result;
    }
}