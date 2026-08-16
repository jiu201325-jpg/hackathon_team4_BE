package com.example.likelionhackathon.dto;

import com.example.likelionhackathon.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String nickname;
    private boolean pregnant;
    private boolean smoking;
    private boolean drinking;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.isPregnant(),
                user.isSmoking(),
                user.isDrinking()
        );
    }
}