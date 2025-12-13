package com.farmapp.backend.dto;

public class ProductDto {
    private Long id;
    private Long farmerId;
    private String name;
    private String category;
    private String unit;
    private Double pricePerUnit;
    private Integer quantityAvailable;
    private String imageUrl;

    public ProductDto() {}

    public ProductDto(Long id, Long farmerId, String name, String category, String unit,
                      Double pricePerUnit, Integer quantityAvailable, String imageUrl) {
        this.id = id;
        this.farmerId = farmerId;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.quantityAvailable = quantityAvailable;
        this.imageUrl = imageUrl;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFarmerId() { return farmerId; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(Double pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    public Integer getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(Integer quantityAvailable) { this.quantityAvailable = quantityAvailable; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
