package com.example.likelionhackathon.controller;

import com.example.likelionhackathon.dto.UserResponse;
import com.example.likelionhackathon.dto.UserUpdateRequest;
import com.example.likelionhackathon.entity.User;
import com.example.likelionhackathon.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getMyInfo(Authentication authentication) {

        User user = userService.getUserByUsername(
                authentication.getName()
        );

        return UserResponse.from(user);
    }

    @PutMapping("/me")
    public UserResponse updateMyInfo(
            Authentication authentication,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        User user = userService.updateUser(
                authentication.getName(),
                request.getNickname(),
                request.isPregnant(),
                request.isSmoking(),
                request.isDrinking()
        );

        return UserResponse.from(user);
    }
}