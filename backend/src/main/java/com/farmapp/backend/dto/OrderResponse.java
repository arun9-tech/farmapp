package com.farmapp.backend.dto;

public class OrderResponse {

    public Long orderId;
    public String status;
    public int quantity;
    public double totalPrice;

    public Product product;
    public Farmer farmer;
    public Customer customer;

    // ===== INNER DTOs =====
    public static class Product {
        public Long id;
        public String name;
        public double pricePerUnit;
        public String unit;
        public String imageUrl;
    }

    public static class Farmer {
        public Long id;
        public String name;
        public String farmName;
        public String location;
    }

    public static class Customer {
        public Long id;
        public String name;
        public String mobile;
    }
}
