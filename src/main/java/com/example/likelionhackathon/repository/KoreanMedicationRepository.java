package com.example.likelionhackathon.repository;

import com.example.likelionhackathon.entity.KoreanMedication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KoreanMedicationRepository extends JpaRepository<KoreanMedication, Long> {
    List<KoreanMedication> findByNameContaining(String query);
}