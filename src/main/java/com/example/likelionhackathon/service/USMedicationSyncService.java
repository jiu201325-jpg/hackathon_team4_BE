package com.example.likelionhackathon.service;

import com.example.likelionhackathon.dto.OpenFdaResponse;
import com.example.likelionhackathon.entity.Ingredient;
import com.example.likelionhackathon.entity.SymptomCategory;
import com.example.likelionhackathon.entity.USMedication;
import com.example.likelionhackathon.repository.IngredientRepository;
import com.example.likelionhackathon.repository.SymptomCategoryRepository;
import com.example.likelionhackathon.repository.USMedicationRepository;
import com.example.likelionhackathon.util.IngredientNameNormalizer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class USMedicationSyncService {

    private final USMedicationRepository usMedicationRepository;
    private final IngredientRepository ingredientRepository;
    private final SymptomCategoryRepository categoryRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public USMedicationSyncService(USMedicationRepository usMedicationRepository,
                                   IngredientRepository ingredientRepository,
                                   SymptomCategoryRepository categoryRepository) {
        this.usMedicationRepository = usMedicationRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    public void syncOne(String brandName) {
        String encodedQuery = URLEncoder.encode("openfda.brand_name:\"" + brandName + "\"", StandardCharsets.UTF_8);
        String urlString = "https://api.fda.gov/drug/label.json?search=" + encodedQuery + "&limit=10";

        URI uri = URI.create(urlString);

        OpenFdaResponse response;
        try {
            response = restTemplate.getForObject(uri, OpenFdaResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("=== openFDA에서 404 (검색결과 없음), 스킵: " + brandName);
            return;
        }

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            System.out.println("=== openFDA 결과 없음: " + brandName);
            return;
        }

        // ↓↓↓ 이 부분이 빠져있던 것 같아 — result를 만드는 로직
        OpenFdaResponse.Result result = response.getResults().stream()
                .filter(r -> r.getOpenfda() != null && r.getOpenfda().getSubstanceName() != null)
                .filter(r -> r.getOpenfda().getBrandName() != null &&
                        r.getOpenfda().getBrandName().stream()
                                .anyMatch(b -> b.equalsIgnoreCase(brandName)))
                .findFirst()
                .orElseGet(() -> response.getResults().stream()
                        .filter(r -> r.getOpenfda() != null && r.getOpenfda().getSubstanceName() != null)
                        .min(Comparator.comparingInt(r -> r.getOpenfda().getSubstanceName().size()))
                        .orElse(null));

        if (result == null) {
            System.out.println("=== substance_name 있는 결과 없음: " + brandName);
            return;
        }

        String resolvedName = result.getOpenfda().getBrandName() != null
                ? String.join(", ", result.getOpenfda().getBrandName()) : brandName;

        if (usMedicationRepository.existsByName(resolvedName)) {
            System.out.println("=== 이미 저장된 제품, 스킵: " + resolvedName);
            return;
        }

        List<String> substanceNames = result.getOpenfda().getSubstanceName();
        System.out.println("=== " + brandName + " → 성분: " + substanceNames);

        List<Ingredient> ingredientList = new ArrayList<>();
        for (String substanceName : substanceNames) {
            String normalizedName = IngredientNameNormalizer.normalize(substanceName);

            Ingredient ingredient = ingredientRepository.findByNameEn(normalizedName)
                    .orElseGet(() -> {
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setNameEn(normalizedName);
                        return ingredientRepository.save(newIngredient);
                    });
            ingredientList.add(ingredient);
        }

        USMedication medication = new USMedication();
        medication.setName(resolvedName);
        medication.setManufacturer(result.getOpenfda().getManufacturerName() != null
                ? String.join(", ", result.getOpenfda().getManufacturerName()) : null);
        medication.setEfficacyText(result.getIndicationsAndUsage() != null
                ? String.join(" ", result.getIndicationsAndUsage()) : null);
        medication.setIngredients(ingredientList);
        medication.setLastVerifiedAt(LocalDate.now());

        usMedicationRepository.save(medication);
    }

    private List<SymptomCategory> extractCategoriesFromEfficacy(String efficacyText) {
        List<SymptomCategory> mappedCategories = new ArrayList<>();
        if (efficacyText == null || efficacyText.trim().isEmpty()) {
            return mappedCategories;
        }

        // 소문자로 변환하여 키워드 매칭의 정확도를 높임
        String lowerEfficacy = efficacyText.toLowerCase();

        // [1] 알러지 (Allergy)
        if (lowerEfficacy.contains("allergy") || lowerEfficacy.contains("allergic") ||
                lowerEfficacy.contains("hypersensitivity") || lowerEfficacy.contains("rhinitis") ||
                lowerEfficacy.contains("itchy") || lowerEfficacy.contains("hives") ||
                lowerEfficacy.contains("rash") || lowerEfficacy.contains("sneezing")) {
            categoryRepository.findByName("알러지").ifPresent(mappedCategories::add);
        }

        // [2] 소화 (Digestion)
        if (lowerEfficacy.contains("stomach") || lowerEfficacy.contains("indigestion") ||
                lowerEfficacy.contains("heartburn") || lowerEfficacy.contains("nausea") ||
                lowerEfficacy.contains("acid") || lowerEfficacy.contains("upset") ||
                lowerEfficacy.contains("diarrhea") || lowerEfficacy.contains("constipation") ||
                lowerEfficacy.contains("bowel") || lowerEfficacy.contains("gastric") ||
                lowerEfficacy.contains("vomiting")) {
            categoryRepository.findByName("소화").ifPresent(mappedCategories::add);
        }

        // [3] 염증 (Inflammation)
        if (lowerEfficacy.contains("inflammation") || lowerEfficacy.contains("inflammatory") ||
                lowerEfficacy.contains("swelling") || lowerEfficacy.contains("arthritis") ||
                lowerEfficacy.contains("redness") || lowerEfficacy.contains("joint") ||
                lowerEfficacy.contains("muscle") || lowerEfficacy.contains("sprain")) {
            categoryRepository.findByName("염증").ifPresent(mappedCategories::add);
        }

        // [4] 두통 / 통증 완화 (Headache / Pain Relief)
        if (lowerEfficacy.contains("headache") || lowerEfficacy.contains("migraine") ||
                lowerEfficacy.contains("ache") || lowerEfficacy.contains("pain") ||
                lowerEfficacy.contains("cramp") || lowerEfficacy.contains("tension")) {
            categoryRepository.findByName("두통").ifPresent(mappedCategories::add);
        }

        // [5] 감기 (Cold)
        if (lowerEfficacy.contains("cold") || lowerEfficacy.contains("cough") ||
                lowerEfficacy.contains("flu") || lowerEfficacy.contains("fever") ||
                lowerEfficacy.contains("sore throat") || lowerEfficacy.contains("congestion") ||
                lowerEfficacy.contains("nasal") || lowerEfficacy.contains("runny nose") ||
                lowerEfficacy.contains("phlegm")) {
            categoryRepository.findByName("감기").ifPresent(mappedCategories::add);
        }

        return mappedCategories;
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}