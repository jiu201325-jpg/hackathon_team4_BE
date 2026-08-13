package com.example.likelionhackathon.util;

public class IngredientNameNormalizer {
    // 모든 성분명을 "첫 글자만 대문자, 나머지 소문자"로 통일
    public static String normalize(String rawName) {
        if (rawName == null || rawName.isBlank()) return rawName;
        String trimmed = rawName.trim();
        return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
    }
}