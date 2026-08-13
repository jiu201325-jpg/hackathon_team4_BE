package com.example.likelionhackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OpenFdaResponse {
    @JsonProperty("results")
    private List<Result> results;
    public List<Result> getResults() { return results; }
    public void setResults(List<Result> results) { this.results = results; }

    public static class Result {
        @JsonProperty("active_ingredient")
        private List<String> activeIngredient;
        @JsonProperty("purpose")
        private List<String> purpose;
        @JsonProperty("indications_and_usage")
        private List<String> indicationsAndUsage;
        @JsonProperty("openfda")
        private Meta openfda;

        public List<String> getActiveIngredient() { return activeIngredient; }
        public void setActiveIngredient(List<String> activeIngredient) { this.activeIngredient = activeIngredient; }
        public List<String> getPurpose() { return purpose; }
        public void setPurpose(List<String> purpose) { this.purpose = purpose; }
        public List<String> getIndicationsAndUsage() { return indicationsAndUsage; }
        public void setIndicationsAndUsage(List<String> indicationsAndUsage) { this.indicationsAndUsage = indicationsAndUsage; }
        public Meta getOpenfda() { return openfda; }
        public void setOpenfda(Meta openfda) { this.openfda = openfda; }
    }

    public static class Meta {
        @JsonProperty("brand_name")
        private List<String> brandName;
        @JsonProperty("manufacturer_name")
        private List<String> manufacturerName;
        @JsonProperty("substance_name")
        private List<String> substanceName;

        public List<String> getBrandName() { return brandName; }
        public void setBrandName(List<String> brandName) { this.brandName = brandName; }
        public List<String> getManufacturerName() { return manufacturerName; }
        public void setManufacturerName(List<String> manufacturerName) { this.manufacturerName = manufacturerName; }
        public List<String> getSubstanceName() { return substanceName; }
        public void setSubstanceName(List<String> substanceName) { this.substanceName = substanceName; }
    }
}