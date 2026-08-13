package com.example.likelionhackathon.service;

import com.example.likelionhackathon.entity.SymptomCategory;
import com.example.likelionhackathon.repository.SymptomCategoryRepository;
import com.example.likelionhackathon.repository.USMedicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SymptomCategoryTaggingService {

    private final SymptomCategoryRepository symptomCategoryRepository;
    private final USMedicationRepository usMedicationRepository;

    public SymptomCategoryTaggingService(SymptomCategoryRepository symptomCategoryRepository,
                                         USMedicationRepository usMedicationRepository) {
        this.symptomCategoryRepository = symptomCategoryRepository;
        this.usMedicationRepository = usMedicationRepository;
    }

    public void initCategoriesAndTag() {
        // 1. 카테고리 5개 생성 (없으면)
        String[] categoryNames = {"알러지", "소화", "염증", "두통", "감기"};
        for (String name : categoryNames) {
            symptomCategoryRepository.findByName(name)
                    .orElseGet(() -> {
                        SymptomCategory c = new SymptomCategory();
                        c.setName(name);
                        return symptomCategoryRepository.save(c);
                    });
        }

        // 제품별로 태깅
        tag("Tylenol", "두통", "염증");
        tag("Advil", "두통", "염증");
        tag("Aleve", "두통", "염증");
        tag("Motrin", "두통", "염증");
        tag("Excedrin", "두통");
        tag("Zyrtec", "알러지");
        tag("Claritin", "알러지");
        tag("Benadryl", "알러지");
        tag("Allegra", "알러지");
        tag("Flonase", "알러지");
        tag("Pepto-Bismol", "소화");
        tag("Tums", "소화");
        tag("Imodium", "소화");
        tag("Gas-X", "소화");
        tag("Pepcid", "소화");
        tag("Dulcolax", "소화");
        tag("Sudafed", "감기");
        tag("Mucinex", "감기");
        tag("DayQuil", "감기");
        tag("Robitussin", "감기");
        tag("Vicks", "감기");
    }

    private void tag(String medicationNameKeyword, String... categoryNames) {
        usMedicationRepository.findAll().stream()
                .filter(m -> m.getName() != null && m.getName().toLowerCase()
                        .contains(medicationNameKeyword.toLowerCase()))
                .forEach(medication -> {
                    List<SymptomCategory> categories = new ArrayList<>();
                    for (String catName : categoryNames) {
                        symptomCategoryRepository.findByName(catName).ifPresent(categories::add);
                    }
                    medication.setCategories(categories);
                    usMedicationRepository.save(medication);
                    System.out.println("=== " + medication.getName() + " → 카테고리: " + Arrays.toString(categoryNames));
                });
    }
}
