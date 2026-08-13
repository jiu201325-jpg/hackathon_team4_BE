package com.example.likelionhackathon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RxNormIdGroupResponse {
    @JsonProperty("idGroup")
    private IdGroup idGroup;
    public IdGroup getIdGroup() { return idGroup; }
    public void setIdGroup(IdGroup idGroup) { this.idGroup = idGroup; }

    public static class IdGroup {
        @JsonProperty("rxnormId")
        private List<String> rxnormId;
        public List<String> getRxnormId() { return rxnormId; }
        public void setRxnormId(List<String> rxnormId) { this.rxnormId = rxnormId; }
    }
}