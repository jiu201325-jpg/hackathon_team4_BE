package com.example.likelionhackathon.service;

import com.example.likelionhackathon.dto.UserMedicationResponse;
import com.example.likelionhackathon.entity.KoreanMedication;
import com.example.likelionhackathon.entity.User;
import com.example.likelionhackathon.entity.UserMedication;
import com.example.likelionhackathon.repository.KoreanMedicationRepository;
import com.example.likelionhackathon.repository.UserMedicationRepository;
import com.example.likelionhackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMedicationService {

    private final UserMedicationRepository userMedicationRepository;
    private final UserRepository userRepository;
    private final KoreanMedicationRepository koreanMedicationRepository;

    @Transactional
    public void addMedication(String username, Long koreanMedicationId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        KoreanMedication koreanMedication =
                koreanMedicationRepository.findById(koreanMedicationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("약 정보를 찾을 수 없습니다.")
                        );

        if (userMedicationRepository.existsByUserAndKoreanMedication(
                user,
                koreanMedication
        )) {
            throw new IllegalArgumentException("이미 등록된 복용약입니다.");
        }

        UserMedication userMedication = UserMedication.builder()
                .user(user)
                .koreanMedication(koreanMedication)
                .build();

        userMedicationRepository.save(userMedication);
    }

    @Transactional(readOnly = true)
    public List<UserMedicationResponse> getMedications(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        return userMedicationRepository.findAllByUser(user)
                .stream()
                .map(UserMedicationResponse::from)
                .toList();
    }

    @Transactional
    public void deleteMedication(String username, Long userMedicationId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        UserMedication userMedication =
                userMedicationRepository.findByIdAndUser(userMedicationId, user)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "등록된 복용약을 찾을 수 없습니다."
                                )
                        );

        userMedicationRepository.delete(userMedication);
    }
}