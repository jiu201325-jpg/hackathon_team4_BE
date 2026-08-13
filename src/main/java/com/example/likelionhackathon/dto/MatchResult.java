package com.example.likelionhackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public class MatchResult {
    @JsonProperty("us_medication")
    private USMedicationDto usMedication;

    @JsonProperty("match_status")
    private String matchStatus; // 겹침 | 추가확인 | 겹침없음 | 확인불가

    @JsonProperty("matched_ingredients")
    private List<IngredientDto> matchedIngredients;

    private String reason;
    private String source;

    @JsonProperty("last_verified_at")
    private LocalDate lastVerifiedAt;

    // getter/setter 전부
    public USMedicationDto getUsMedication() { return usMedication; }
    public void setUsMedication(USMedicationDto usMedication) { this.usMedication = usMedication; }
    public String getMatchStatus() { return matchStatus; }
    public void setMatchStatus(String matchStatus) { this.matchStatus = matchStatus; }
    public List<IngredientDto> getMatchedIngredients() { return matchedIngredients; }
    public void setMatchedIngredients(List<IngredientDto> matchedIngredients) { this.matchedIngredients = matchedIngredients; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public LocalDate getLastVerifiedAt() { return lastVerifiedAt; }
    public void setLastVerifiedAt(LocalDate lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; }
}