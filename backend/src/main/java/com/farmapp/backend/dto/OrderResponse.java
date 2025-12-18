package com.farmapp.backend.dto;

public class OrderResponse {

    public Long orderId;

    // product info
    public Long productId;
    public String productName;
    public double pricePerUnit;

    // farmer info
    public Long farmerId;
    public String farmerName;
    public String farmName;

    // customer info
    public Long customerId;
    public String customerName;
    public String customerMobile;

    // order info
    public int quantity;
    public double totalPrice;
    public String status;
}
