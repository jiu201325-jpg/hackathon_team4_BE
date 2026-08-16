package com.example.likelionhackathon.util;

public class IngredientNameNormalizer {
    private static final String[] NOISE_WORDS = {
            "granules", "micronized", "unspecified", "anhydrous"
    };

    public static String normalize(String rawName) {
        if (rawName == null || rawName.isBlank()) return rawName;
        String cleaned = rawName.trim();

        for (String noise : NOISE_WORDS) {
            cleaned = cleaned.replaceAll("(?i)[,]?\\s*\\b" + noise + "\\b", "");
        }
        cleaned = cleaned.trim().replaceAll("\\s+", " ");

        if (cleaned.isEmpty()) return rawName; // 다 지워지면 원본 유지
        return cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1).toLowerCase();
    }
}