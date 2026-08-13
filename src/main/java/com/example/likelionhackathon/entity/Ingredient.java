package com.example.likelionhackathon.entity;

import jakarta.persistence.*;

@Entity
public class Ingredient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameKr;
    private String nameEn;
    private String standardCode;
    private String atcClass;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNameKr() { return nameKr; }
    public void setNameKr(String nameKr) { this.nameKr = nameKr; }

    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }

    public String getStandardCode() { return standardCode; }
    public void setStandardCode(String standardCode) { this.standardCode = standardCode; }

    public String getAtcClass() { return atcClass; }
    public void setAtcClass(String atcClass) { this.atcClass = atcClass; }
}