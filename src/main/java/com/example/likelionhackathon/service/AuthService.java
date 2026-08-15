package com.example.likelionhackathon.service;

import com.example.likelionhackathon.dto.LoginRequest;
import com.example.likelionhackathon.dto.LoginResponse;
import com.example.likelionhackathon.dto.SignupRequest;
import com.example.likelionhackathon.entity.User;
import com.example.likelionhackathon.exception.DuplicateUsernameException;
import com.example.likelionhackathon.exception.InvalidPasswordException;
import com.example.likelionhackathon.exception.UserNotFoundException;
import com.example.likelionhackathon.repository.UserRepository;
import com.example.likelionhackathon.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequest request) {

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException();
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtTokenProvider.createToken(
                user.getId(),
                user.getUsername()
        );

        return new LoginResponse(accessToken);
    }
}