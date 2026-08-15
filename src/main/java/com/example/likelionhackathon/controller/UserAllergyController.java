package com.example.likelionhackathon.controller;

import com.example.likelionhackathon.dto.AllergyAddRequest;
import com.example.likelionhackathon.dto.UserAllergyResponse;
import com.example.likelionhackathon.service.UserAllergyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/allergies")
@RequiredArgsConstructor
public class UserAllergyController {

    private final UserAllergyService userAllergyService;

    @GetMapping
    public ResponseEntity<List<UserAllergyResponse>> getAllergies(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                userAllergyService.getAllergies(authentication.getName())
        );
    }

    @PostMapping
    public ResponseEntity<Void> addAllergy(
            Authentication authentication,
            @Valid @RequestBody AllergyAddRequest request
    ) {
        userAllergyService.addAllergy(
                authentication.getName(),
                request.getAllergyName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllergy(
            Authentication authentication,
            @PathVariable Long id
    ) {
        userAllergyService.deleteAllergy(
                authentication.getName(),
                id
        );

        return ResponseEntity.noContent().build();
    }
}