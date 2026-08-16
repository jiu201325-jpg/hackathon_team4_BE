package com.example.likelionhackathon.controller;

import com.example.likelionhackathon.dto.UserMedicationResponse;

import java.util.List;

import com.example.likelionhackathon.dto.MedicationAddRequest;
import com.example.likelionhackathon.service.UserMedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me/medications")
@RequiredArgsConstructor
public class UserMedicationController {

    private final UserMedicationService userMedicationService;

    @PostMapping
    public ResponseEntity<Void> addMedication(
            Authentication authentication,
            @Valid @RequestBody MedicationAddRequest request
    ) {
        userMedicationService.addMedication(
                authentication.getName(),
                request.getKoreanMedicationId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping
    public ResponseEntity<List<UserMedicationResponse>> getMedications(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                userMedicationService.getMedications(authentication.getName())
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(
            Authentication authentication,
            @PathVariable Long id
    ) {
        userMedicationService.deleteMedication(
                authentication.getName(),
                id
        );

        return ResponseEntity.noContent().build();
    }
}