package com.example.likelionhackathon.repository;

import com.example.likelionhackathon.entity.USMedication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface USMedicationRepository extends JpaRepository<USMedication, Long> {
    boolean existsByName(String name);
    List<USMedication> findByCategories_Id(Long categoryId);
}

