package com.example.likelionhackathon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MedicationAddRequest {

    @NotNull(message = "약 ID는 필수입니다.")
    private Long koreanMedicationId;
}