package com.example.likelionhackathon.entity;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDate;

@Entity
public class KoreanMedication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String source;

    private LocalDate lastVerifiedAt;




    @ManyToMany
    private List<Ingredient> ingredients;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDate getLastVerifiedAt() { return lastVerifiedAt; }

    public void setLastVerifiedAt(LocalDate lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; }

}