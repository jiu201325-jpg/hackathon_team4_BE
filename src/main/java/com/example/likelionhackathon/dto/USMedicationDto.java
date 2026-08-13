package com.example.likelionhackathon.dto;

public class USMedicationDto {
    private Long id;
    private String name;
    private String manufacturer;
    private String imageUrl;
    private String efficacyText;
    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getEfficacyText() { return efficacyText; }
    public void setEfficacyText(String efficacyText) { this.efficacyText = efficacyText; }
}