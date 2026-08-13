package com.example.likelionhackathon.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class USMedication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String manufacturer;

    @Column(length = 2000)
    private String efficacyText; // indications_and_usage

    private String imageUrl; // 수작업 등록 예정
    private String source = "openFDA";
    private LocalDate lastVerifiedAt;

    @ManyToMany
    @JoinTable(
            name = "us_medication_ingredient",
            joinColumns = @JoinColumn(name = "us_medication_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<Ingredient> ingredients;

    @ManyToMany
    @JoinTable(
            name = "us_medication_category",
            joinColumns = @JoinColumn(name = "us_medication_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<SymptomCategory> categories = new ArrayList<>(); //초기화 설정

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getEfficacyText() { return efficacyText; }
    public void setEfficacyText(String efficacyText) { this.efficacyText = efficacyText; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDate getLastVerifiedAt() { return lastVerifiedAt; }
    public void setLastVerifiedAt(LocalDate lastVerifiedAt) { this.lastVerifiedAt = lastVerifiedAt; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public List<SymptomCategory> getCategories() { return categories; }
    public void setCategories(List<SymptomCategory> categories) { this.categories = categories; }
}