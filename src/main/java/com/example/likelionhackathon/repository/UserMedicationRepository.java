package com.example.likelionhackathon.repository;

import com.example.likelionhackathon.entity.KoreanMedication;
import com.example.likelionhackathon.entity.User;
import com.example.likelionhackathon.entity.UserMedication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMedicationRepository extends JpaRepository<UserMedication, Long> {

    List<UserMedication> findAllByUser(User user);

    Optional<UserMedication> findByIdAndUser(Long id, User user);

    boolean existsByUserAndKoreanMedication(
            User user,
            KoreanMedication koreanMedication
    );
}