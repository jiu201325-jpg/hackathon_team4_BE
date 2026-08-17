package com.example.likelionhackathon.config;

import com.example.likelionhackathon.repository.KoreanMedicationRepository;
import com.example.likelionhackathon.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final KoreanMedicationRepository koreanMedicationRepository;
    private final KoreanMedicationSyncService koreanMedicationSyncService;
    private final USMedicationSyncService usMedicationSyncService;
    private final SymptomCategoryTaggingService symptomCategoryTaggingService;
    private final IngredientEnrichmentService ingredientEnrichmentService;

    public DataSeeder(KoreanMedicationRepository koreanMedicationRepository,
                      KoreanMedicationSyncService koreanMedicationSyncService,
                      USMedicationSyncService usMedicationSyncService,
                      SymptomCategoryTaggingService symptomCategoryTaggingService,
                      IngredientEnrichmentService ingredientEnrichmentService) {
        this.koreanMedicationRepository = koreanMedicationRepository;
        this.koreanMedicationSyncService = koreanMedicationSyncService;
        this.usMedicationSyncService = usMedicationSyncService;
        this.symptomCategoryTaggingService = symptomCategoryTaggingService;
        this.ingredientEnrichmentService = ingredientEnrichmentService;
    }

    @Override
    public void run(String... args) {
        if (koreanMedicationRepository.count() > 0) {
            System.out.println("=== 데이터 이미 존재, 시딩 스킵");
            return;
        }

        System.out.println("=== 서버 시작 - 데이터 시딩 시작");

        String[] koreanMedications = {
                "타이레놀", "이지엔6", "낙센",
                "지르텍", "클라리틴", "알레그라",
                "베아제", "개비스콘", "스멕타"
        };
        for (String medication : koreanMedications) {
            koreanMedicationSyncService.syncOne(medication);
        }

        String[] usMedications = {
                "Tylenol", "Advil", "Aleve", "Zyrtec", "Claritin", "Benadryl",
                "Pepto-Bismol", "Tums", "Imodium", "Sudafed", "Mucinex",
                "Motrin", "Excedrin", "Allegra", "Flonase", "Gas-X",
                "Pepcid", "Dulcolax", "DayQuil", "Robitussin Cough", "Theraflu"
        };
        for (String medication : usMedications) {
            usMedicationSyncService.syncOne(medication);
        }

        symptomCategoryTaggingService.initCategoriesAndTag();
        ingredientEnrichmentService.enrichAll();

        System.out.println("=== 데이터 시딩 완료");
    }
}
