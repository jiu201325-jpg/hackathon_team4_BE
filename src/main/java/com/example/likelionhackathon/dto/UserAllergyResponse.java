package com.example.likelionhackathon.dto;

import com.example.likelionhackathon.entity.UserAllergy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAllergyResponse {

    private Long id;
    private String allergyName;

    public static UserAllergyResponse from(UserAllergy userAllergy) {
        return new UserAllergyResponse(
                userAllergy.getId(),
                userAllergy.getAllergyName()
        );
    }
}