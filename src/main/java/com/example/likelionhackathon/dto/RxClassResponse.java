package com.example.likelionhackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RxClassResponse {
    @JsonProperty("rxclassDrugInfoList")
    private RxclassDrugInfoList rxclassDrugInfoList;
    public RxclassDrugInfoList getRxclassDrugInfoList() { return rxclassDrugInfoList; }
    public void setRxclassDrugInfoList(RxclassDrugInfoList rxclassDrugInfoList) { this.rxclassDrugInfoList = rxclassDrugInfoList; }

    public static class RxclassDrugInfoList {
        @JsonProperty("rxclassDrugInfo")
        private List<RxclassDrugInfo> rxclassDrugInfo;
        public List<RxclassDrugInfo> getRxclassDrugInfo() { return rxclassDrugInfo; }
        public void setRxclassDrugInfo(List<RxclassDrugInfo> rxclassDrugInfo) { this.rxclassDrugInfo = rxclassDrugInfo; }
    }

    public static class RxclassDrugInfo {
        @JsonProperty("rxclassMinConceptItem")
        private RxclassMinConceptItem rxclassMinConceptItem;
        public RxclassMinConceptItem getRxclassMinConceptItem() { return rxclassMinConceptItem; }
        public void setRxclassMinConceptItem(RxclassMinConceptItem rxclassMinConceptItem) { this.rxclassMinConceptItem = rxclassMinConceptItem; }
    }

    public static class RxclassMinConceptItem {
        @JsonProperty("classId")
        private String classId; // ATC 코드
        public String getClassId() { return classId; }
        public void setClassId(String classId) { this.classId = classId; }
    }
}