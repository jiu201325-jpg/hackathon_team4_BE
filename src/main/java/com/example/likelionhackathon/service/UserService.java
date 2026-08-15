package com.example.likelionhackathon.service;

import com.example.likelionhackathon.entity.User;
import com.example.likelionhackathon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );
    }

    @Transactional
    public User updateUser(
            String username,
            String nickname,
            boolean pregnant,
            boolean smoking,
            boolean drinking
    ) {
        User user = getUserByUsername(username);

        user.setNickname(nickname);
        user.setPregnant(pregnant);
        user.setSmoking(smoking);
        user.setDrinking(drinking);

        return user;
    }
}