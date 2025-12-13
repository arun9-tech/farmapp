package com.farmapp.backend.dto;

import lombok.Data;

@Data
public class FarmerProfileUpdateRequest {
    private String name;
    private String phone;
    private String address;
    private String deliveryPreference;
}
