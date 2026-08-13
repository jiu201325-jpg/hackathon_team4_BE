package com.example.likelionhackathon.service;

import com.example.likelionhackathon.dto.RxClassResponse;
import com.example.likelionhackathon.dto.RxNormIdGroupResponse;
import com.example.likelionhackathon.entity.Ingredient;
import com.example.likelionhackathon.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class IngredientEnrichmentService {

    private final IngredientRepository ingredientRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public IngredientEnrichmentService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public void enrichAll() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        for (Ingredient ingredient : ingredients) {
            enrichOne(ingredient);
        }
    }

    private void enrichOne(Ingredient ingredient) {
        try {
            // 1. RxNorm CUI 조회
            String encodedName = URLEncoder.encode(ingredient.getNameEn(), StandardCharsets.UTF_8);
            String rxNormUrl = "https://rxnav.nlm.nih.gov/REST/rxcui.json?name=" + encodedName;
            RxNormIdGroupResponse rxNormResponse = restTemplate.getForObject(URI.create(rxNormUrl), RxNormIdGroupResponse.class);

            if (rxNormResponse == null || rxNormResponse.getIdGroup() == null
                    || rxNormResponse.getIdGroup().getRxnormId() == null
                    || rxNormResponse.getIdGroup().getRxnormId().isEmpty()) {
                System.out.println("=== RxNorm CUI 못 찾음: " + ingredient.getNameEn());
                return;
            }

            String cui = rxNormResponse.getIdGroup().getRxnormId().get(0);
            ingredient.setStandardCode(cui);

            // 2. RxClass로 ATC 코드 조회
            String rxClassUrl = "https://rxnav.nlm.nih.gov/REST/rxclass/class/byRxcui.json?rxcui=" + cui + "&relaSource=ATC";
            RxClassResponse rxClassResponse = restTemplate.getForObject(URI.create(rxClassUrl), RxClassResponse.class);

            if (rxClassResponse != null && rxClassResponse.getRxclassDrugInfoList() != null
                    && rxClassResponse.getRxclassDrugInfoList().getRxclassDrugInfo() != null
                    && !rxClassResponse.getRxclassDrugInfoList().getRxclassDrugInfo().isEmpty()) {
                String atcClass = rxClassResponse.getRxclassDrugInfoList().getRxclassDrugInfo()
                        .get(0).getRxclassMinConceptItem().getClassId();
                ingredient.setAtcClass(atcClass);
                System.out.println("=== " + ingredient.getNameEn() + " → CUI: " + cui + ", ATC: " + atcClass);
            } else {
                System.out.println("=== " + ingredient.getNameEn() + " → CUI: " + cui + ", ATC 없음");
            }

            ingredientRepository.save(ingredient);

        } catch (Exception e) {
            System.out.println("=== 에러 발생: " + ingredient.getNameEn() + " → " + e.getMessage());
        }
    }
}