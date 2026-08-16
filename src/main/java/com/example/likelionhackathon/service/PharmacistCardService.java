package com.example.likelionhackathon.service;

import com.example.likelionhackathon.dto.PharmacistCardRequest;
import com.example.likelionhackathon.dto.UserAllergyResponse;
import com.example.likelionhackathon.dto.UserMedicationResponse;
import com.example.likelionhackathon.entity.User;
import com.example.likelionhackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacistCardService {

    private final UserRepository userRepository;
    private final UserAllergyService userAllergyService;
    private final UserMedicationService userMedicationService;

    @Transactional(readOnly = true)
    public String createCard(
            String username,
            PharmacistCardRequest request
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        List<UserAllergyResponse> allergies =
                userAllergyService.getAllergies(username);

        List<UserMedicationResponse> medications =
                userMedicationService.getMedications(username);

        StringBuilder card = new StringBuilder();

        card.append("Hello, I am a traveler from Korea.\n\n");

        // 현재 증상
        card.append("My current symptoms: ")
                .append(request.getSymptom() == null || request.getSymptom().isBlank()
                        ? "Not specified"
                        : request.getSymptom())
                .append("\n\n");

        // 복용 중인 약
        card.append("Medications I am currently taking:\n");

        if (medications.isEmpty()) {
            card.append("- None reported\n");
        } else {
            for (UserMedicationResponse medication : medications) {
                card.append("- ")
                        .append(medication.getName())
                        .append("\n");
            }
        }

        card.append("\n");

        // 알레르기
        card.append("Allergies:\n");

        if (allergies.isEmpty()) {
            card.append("- None reported\n");
        } else {
            for (UserAllergyResponse allergy : allergies) {
                card.append("- ")
                        .append(allergy.getAllergyName())
                        .append("\n");
            }
        }

        card.append("\n");

        // 기타 주의사항
        card.append("Other information:\n");

        boolean hasOtherInformation = false;

        if (user.isPregnant()) {
            card.append("- Pregnant\n");
            hasOtherInformation = true;
        }

        if (user.isSmoking()) {
            card.append("- Smoker\n");
            hasOtherInformation = true;
        }

        if (user.isDrinking()) {
            card.append("- Drinks alcohol\n");
            hasOtherInformation = true;
        }

        if (!hasOtherInformation) {
            card.append("- None reported\n");
        }

        card.append("\n");

        // 약사님께 보여드릴 안내 문장
        card.append(
                "Please check my current medications, allergies, "
                        + "and other health information before recommending any medicine."
        );

        return card.toString();
    }
}