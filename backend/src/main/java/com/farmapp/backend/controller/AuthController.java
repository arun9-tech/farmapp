package com.farmapp.backend.controller;

import com.farmapp.backend.dto.AuthResponse;
import com.farmapp.backend.dto.LoginRequest;
import com.farmapp.backend.dto.SignupRequest;
import com.farmapp.backend.model.Farmer;
import com.farmapp.backend.repository.FarmerRepository;
import com.farmapp.backend.security.JwtUtil;
import com.farmapp.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final FarmerRepository farmerRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService,
                          FarmerRepository farmerRepository,
                          JwtUtil jwtUtil) {
        this.authService = authService;
        this.farmerRepository = farmerRepository;
        this.jwtUtil = jwtUtil;
    }

    // SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        try {
            Farmer saved = authService.signup(req);
            saved.setPasswordHash(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<String> maybeToken = authService.login(req);
        if (maybeToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = maybeToken.get();
        String subject = jwtUtil.getSubject(token);
        Long farmerId;
        try {
            farmerId = Long.valueOf(subject);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid token subject");
        }

        Farmer f = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Farmer not found"));

        AuthResponse resp = new AuthResponse(token, f.getId(), f.getEmail(), f.getName());
        return ResponseEntity.ok(resp);
    }

    // GET CURRENT FARMER (/me)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentFarmer(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authenticated user");
        }

        // Principal.getName() is the farmerId (String) we set in JwtAuthenticationFilter
        Long farmerId;
        try {
            farmerId = Long.parseLong(principal.getName());
        } catch (NumberFormatException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token subject");
        }

        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farmer not found"));

        Map<String, Object> body = new HashMap<>();
        body.put("id", farmer.getId());
        body.put("name", farmer.getName());
        body.put("email", farmer.getEmail());
        body.put("phone", farmer.getPhone());
        body.put("address", farmer.getAddress());
        body.put("deliveryPreference", farmer.getDeliveryPreference());

        return ResponseEntity.ok(body);
    }
}
