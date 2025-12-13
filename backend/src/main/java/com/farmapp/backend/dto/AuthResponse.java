package com.farmapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;       // JWT
    private Long farmerId;
    private String email;
    private String name;
}
