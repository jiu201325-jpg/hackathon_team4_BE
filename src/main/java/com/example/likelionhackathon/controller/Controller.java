package com.example.likelionhackathon.controller;

import com.example.likelionhackathon.dto.MatchRequest;
import com.example.likelionhackathon.dto.MatchResponse;
import com.example.likelionhackathon.repository.KoreanMedicationRepository;
import com.example.likelionhackathon.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    private final KoreanMedicationSyncService koreanMedicationSyncService;
    private final USMedicationSyncService usMedicationSyncService;
    private final SymptomCategoryTaggingService symptomCategoryTaggingService;
    private final IngredientEnrichmentService ingredientEnrichmentService;
    private final MatchService matchService;

    public Controller(KoreanMedicationSyncService koreanMedicationSyncService,
                      USMedicationSyncService usMedicationSyncService,
                      SymptomCategoryTaggingService symptomCategoryTaggingService,
                      IngredientEnrichmentService ingredientEnrichmentService,
                      MatchService matchService ) {
        this.koreanMedicationSyncService = koreanMedicationSyncService;
        this.usMedicationSyncService = usMedicationSyncService;
        this.symptomCategoryTaggingService = symptomCategoryTaggingService;
        this.ingredientEnrichmentService = ingredientEnrichmentService;
        this.matchService = matchService;
    }

    @GetMapping("/test-sync")
    public String testSync() {
        koreanMedicationSyncService.syncOne("판피린");
        return "저장 완료, DB 확인해보세요";
    }

    @GetMapping("/test-sync-us")
    public String testSyncUs() {
        usMedicationSyncService.syncOne("Tylenol");
        return "US 약 저장 완료, DB 확인해보세요";
    }

    @GetMapping("/test-sync-us-batch")
    public String testSyncUsBatch() {
        String[] brands = {"Advil", "Aleve", "Zyrtec", "Claritin", "Benadryl", "Pepto-Bismol", "Tums", "Imodium", "Sudafed", "Mucinex"
        , "Motrin", "Excedrin", "Allegra", "Flonase", "Gas-X", "Pepcid", "Dulcolax", "DayQuil", "Robitussin", "Vicks"};
        for (String brand : brands) {
            usMedicationSyncService.syncOne(brand);
        }
        return "US 약 배치 저장 완료, DB 확인해보세요";
    }

    @GetMapping("/init-categories")
    public String initCategories() {
        symptomCategoryTaggingService.initCategoriesAndTag();
        return "카테고리 생성 및 태깅 완료";
    }

    @GetMapping("/test-sync-kr-batch")
    public String testSyncKrBatch() {
        String[] koreanMedications = { "타이레놀", "이지엔6", "낙센",
                "지르텍", "클라리틴", "알레그라",
                "베아제", "개비스콘", "스멕타"};
        for (String medication : koreanMedications) {
            koreanMedicationSyncService.syncOne(medication);
        }
        return "한국 약 배치 저장 완료, DB 확인해보세요";
    }

    @GetMapping("/enrich-ingredients")
    public String enrichIngredients() {
        ingredientEnrichmentService.enrichAll();
        return "성분 CUI/ATC 매핑 완료";
    }

    @PostMapping("/api/match")
    public MatchResponse match(@RequestBody MatchRequest request) {
        return matchService.compare(request);
    }

    @GetMapping("/init-all")
    public String initAll() {
        // 한국 약
        String[] koreanMedications = {
                "타이레놀", "이지엔6", "낙센",
                "지르텍", "클라리틴", "알레그라",
                "베아제", "개비스콘", "스멕타"
        };
        for (String medication : koreanMedications) {
            koreanMedicationSyncService.syncOne(medication);
        }

        // 미국 약 (Tylenol 포함해서 20개로 통일)
        String[] usMedications = {
                "Tylenol", "Advil", "Aleve", "Zyrtec", "Claritin", "Benadryl",
                "Pepto-Bismol", "Tums", "Imodium", "Sudafed", "Mucinex",
                "Motrin", "Excedrin", "Allegra", "Flonase", "Gas-X",
                "Pepcid", "Dulcolax", "DayQuil", "Robitussin Cough", "Theraflu"
        };
        for (String medication : usMedications) {
            usMedicationSyncService.syncOne(medication);
        }

        // 카테고리 생성 + 태깅
        symptomCategoryTaggingService.initCategoriesAndTag();

        // RxNorm CUI + ATC 채우기
        ingredientEnrichmentService.enrichAll();

        return "전체 초기화 완료 (한국약+미국약+카테고리+CUI/ATC)";
    }

    @RestController
    @RequestMapping("/api/medications")
    public class MedicationController {

        private final KoreanMedicationRepository koreanMedicationRepository;

        public MedicationController(KoreanMedicationRepository koreanMedicationRepository) {
            this.koreanMedicationRepository = koreanMedicationRepository;
        }

        @GetMapping("/korean")
        public List<?> searchKorean(@RequestParam String query) {
            return koreanMedicationRepository.findByNameContaining(query);
        }
    }
}