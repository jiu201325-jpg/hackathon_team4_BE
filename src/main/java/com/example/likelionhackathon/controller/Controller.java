package com.example.likelionhackathon.controller;

import com.example.likelionhackathon.dto.MatchRequest;
import com.example.likelionhackathon.dto.MatchResponse;
import com.example.likelionhackathon.repository.KoreanMedicationRepository;
import com.example.likelionhackathon.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/api/match")
    public ResponseEntity<?> match(@RequestBody MatchRequest request) {
        try {
            return ResponseEntity.ok(matchService.compare(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
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