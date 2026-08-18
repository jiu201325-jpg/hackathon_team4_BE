package com.example.likelionhackathon.service;

import com.example.likelionhackathon.dto.MfdsPrdtPrmsnResponse;
import com.example.likelionhackathon.entity.Ingredient;
import com.example.likelionhackathon.entity.KoreanMedication;
import com.example.likelionhackathon.repository.IngredientRepository;
import com.example.likelionhackathon.repository.KoreanMedicationRepository;
import com.example.likelionhackathon.util.IngredientNameNormalizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

@Service
public class KoreanMedicationSyncService {

    @Value("${mfds.service-key}")
    private String serviceKey;

    private final KoreanMedicationRepository koreanMedicationRepository;
    private final IngredientRepository ingredientRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public KoreanMedicationSyncService(KoreanMedicationRepository koreanMedicationRepository,
                                       IngredientRepository ingredientRepository) {
        this.koreanMedicationRepository = koreanMedicationRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public void syncOne(String query) {
        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
        String encodedItemName = URLEncoder.encode(query, StandardCharsets.UTF_8);

        String urlString = "http://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService07/getDrugPrdtPrmsnInq07"
                + "?serviceKey=" + encodedServiceKey
                + "&item_name=" + encodedItemName
                + "&type=json";

        URI uri = URI.create(urlString);

        MfdsPrdtPrmsnResponse response = restTemplate.getForObject(uri, MfdsPrdtPrmsnResponse.class);
        if (response == null || response.getBody() == null) {
            System.out.println("=== 응답이 비어있음");
            return;
        }

        System.out.println("=== 받은 아이템 개수: " + response.getBody().getItems().size());

        for (MfdsPrdtPrmsnResponse.Item item : response.getBody().getItems()) {
            if (item.getItemIngrName() == null) {
                System.out.println("=== 성분 정보 없음, 스킵: " + item.getItemName());
                continue;
            }

            String[] ingredientNames = item.getItemIngrName().split("/");
            System.out.println("=== " + item.getItemName() + " → 성분: " + item.getItemIngrName());

            List<Ingredient> ingredientList = new ArrayList<>();
            for (String trimmedName : ingredientNames) {
                String normalizedName = IngredientNameNormalizer.normalize(trimmedName);
                Ingredient ingredient = ingredientRepository.findByNameEn(normalizedName)
                        .orElseGet(() -> {
                            Ingredient newIngredient = new Ingredient();
                            newIngredient.setNameEn(normalizedName);
                            return ingredientRepository.save(newIngredient);
                        });
                ingredientList.add(ingredient);
            }

            KoreanMedication medication = new KoreanMedication();
            medication.setName(item.getItemName());
            medication.setSource("식약처");
            medication.setIngredients(ingredientList);
            medication.setLastVerifiedAt(LocalDate.now());
            koreanMedicationRepository.save(medication);
        }
    }
}