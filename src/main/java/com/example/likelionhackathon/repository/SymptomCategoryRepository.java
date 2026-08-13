package com.example.likelionhackathon.repository;

import com.example.likelionhackathon.entity.SymptomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SymptomCategoryRepository extends JpaRepository<SymptomCategory, Long> {
    Optional<SymptomCategory> findByName(String name);
}