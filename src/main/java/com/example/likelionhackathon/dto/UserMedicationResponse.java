package com.example.likelionhackathon.dto;

import com.example.likelionhackathon.entity.UserMedication;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMedicationResponse {

    private Long id;
    private Long koreanMedicationId;
    private String name;

    public static UserMedicationResponse from(UserMedication userMedication) {
        return new UserMedicationResponse(
                userMedication.getId(),
                userMedication.getKoreanMedication().getId(),
                userMedication.getKoreanMedication().getName()
        );
    }
}