package com.farmapp.backend.dto;

public class ProductResponse {

    public Long id;
    public String name;
    public String category;
    public String unit;
    public double pricePerUnit;
    public int quantityAvailable;
    public String imageUrl;

    public Long farmerId;
    public String farmerName;
    public String farmName;
    public String location;
}
