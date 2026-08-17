package com.example.likelionhackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PharmacistCardRequest {

    @JsonProperty("korean_medication_ids")
    private List<Long> koreanMedicationIds;

    @JsonProperty("us_medication_ids")
    private List<Long> usMedicationIds;

    @JsonProperty("symptom_category_id")
    private Long symptomCategoryId;

    private String symptom;

    public PharmacistCardRequest() {
    }

    public List<Long> getKoreanMedicationIds() {
        return koreanMedicationIds;
    }

    public void setKoreanMedicationIds(List<Long> koreanMedicationIds) {
        this.koreanMedicationIds = koreanMedicationIds;
    }

    public List<Long> getUsMedicationIds() {
        return usMedicationIds;
    }

    public void setUsMedicationIds(List<Long> usMedicationIds) {
        this.usMedicationIds = usMedicationIds;
    }

    public Long getSymptomCategoryId() {
        return symptomCategoryId;
    }

    public void setSymptomCategoryId(Long symptomCategoryId) {
        this.symptomCategoryId = symptomCategoryId;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }
}