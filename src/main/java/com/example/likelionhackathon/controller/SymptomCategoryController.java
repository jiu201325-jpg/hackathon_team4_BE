package com.example.likelionhackathon.controller;

import com.example.likelionhackathon.repository.SymptomCategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SymptomCategoryController {

    private final SymptomCategoryRepository symptomCategoryRepository;

    public SymptomCategoryController(SymptomCategoryRepository symptomCategoryRepository) {
        this.symptomCategoryRepository = symptomCategoryRepository;
    }

    @GetMapping("/api/symptom-categories")
    public Map<String, Object> getCategories() {
        List<Map<String, Object>> categories = symptomCategoryRepository.findAll().stream()
                .map(c -> Map.<String, Object>of("id", c.getId(), "name", c.getName()))
                .collect(Collectors.toList());

        return Map.of("categories", categories);
    }
}