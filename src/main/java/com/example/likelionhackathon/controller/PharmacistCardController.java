package com.example.likelionhackathon.controller;

import com.example.likelionhackathon.dto.PharmacistCardRequest;
import com.example.likelionhackathon.service.PharmacistCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pharmacist-card")
@RequiredArgsConstructor
public class PharmacistCardController {

    private final PharmacistCardService pharmacistCardService;

    @PostMapping
    public ResponseEntity<String> createCard(
            Authentication authentication,
            @Valid @RequestBody PharmacistCardRequest request
    ) {
        String card = pharmacistCardService.createCard(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(card);
    }
}