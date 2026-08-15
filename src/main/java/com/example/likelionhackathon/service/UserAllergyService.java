package com.example.likelionhackathon.service;

import com.example.likelionhackathon.dto.UserAllergyResponse;
import com.example.likelionhackathon.entity.User;
import com.example.likelionhackathon.entity.UserAllergy;
import com.example.likelionhackathon.repository.UserAllergyRepository;
import com.example.likelionhackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAllergyService {

    private final UserAllergyRepository userAllergyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addAllergy(String username, String allergyName) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        UserAllergy userAllergy = UserAllergy.builder()
                .user(user)
                .allergyName(allergyName)
                .build();

        userAllergyRepository.save(userAllergy);
    }

    @Transactional(readOnly = true)
    public List<UserAllergyResponse> getAllergies(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        return userAllergyRepository.findAllByUser(user)
                .stream()
                .map(UserAllergyResponse::from)
                .toList();
    }

    @Transactional
    public void deleteAllergy(String username, Long allergyId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        UserAllergy allergy = userAllergyRepository
                .findById(allergyId)
                .orElseThrow(() ->
                        new IllegalArgumentException("알레르기 정보를 찾을 수 없습니다.")
                );

        if (!allergy.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제할 수 없는 알레르기 정보입니다.");
        }

        userAllergyRepository.delete(allergy);
    }
}