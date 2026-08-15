package com.example.likelionhackathon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    private boolean pregnant;
    private boolean smoking;
    private boolean drinking;
}