package com.farmapp.backend.dto;

import lombok.Data;

@Data
public class FarmerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
