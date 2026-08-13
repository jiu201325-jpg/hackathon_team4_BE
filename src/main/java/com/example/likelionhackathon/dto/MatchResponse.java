package com.example.likelionhackathon.dto;

import java.util.List;

public class MatchResponse {
    private List<MatchResult> results;
    public List<MatchResult> getResults() { return results; }
    public void setResults(List<MatchResult> results) { this.results = results; }
}