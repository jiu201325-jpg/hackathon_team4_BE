package com.example.likelionhackathon.repository;

import com.example.likelionhackathon.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByNameKr(String nameKr);
    Optional<Ingredient> findByNameEn(String nameEn);
}