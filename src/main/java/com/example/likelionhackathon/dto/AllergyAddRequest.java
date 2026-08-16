package com.example.likelionhackathon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AllergyAddRequest {

    @NotBlank(message = "알레르기 이름은 필수입니다.")
    private String allergyName;
}