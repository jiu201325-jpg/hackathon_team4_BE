package com.example.likelionhackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MatchRequest {
    @JsonProperty("korean_medication_ids")
    private List<Long> koreanMedicationIds;

    @JsonProperty("symptom_category_id")
    private Long symptomCategoryId;

    public List<Long> getKoreanMedicationIds() { return koreanMedicationIds; }
    public void setKoreanMedicationIds(List<Long> koreanMedicationIds) { this.koreanMedicationIds = koreanMedicationIds; }
    public Long getSymptomCategoryId() { return symptomCategoryId; }
    public void setSymptomCategoryId(Long symptomCategoryId) { this.symptomCategoryId = symptomCategoryId; }
}