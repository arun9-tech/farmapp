package com.farmapp.backend.service;

import com.farmapp.backend.dto.LoginRequest;
import com.farmapp.backend.dto.SignupRequest;
import com.farmapp.backend.model.Farmer;
import com.farmapp.backend.repository.FarmerRepository;
import com.farmapp.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(FarmerRepository farmerRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.farmerRepository = farmerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new farmer. Throws IllegalArgumentException if email already exists.
     */
    @Transactional
    public Farmer signup(SignupRequest req) {
        // simple validation: check existing email
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (farmerRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        Farmer f = new Farmer();
        f.setName(req.getName());
        f.setEmail(req.getEmail());
        f.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        f.setPhone(req.getPhone());
        f.setAddress(req.getAddress());
        f.setDeliveryPreference(req.getDeliveryPreference());
        return farmerRepository.save(f);
    }

    /**
     * Authenticate and return JWT token (subject = farmer id) if successful.
     */
    public Optional<String> login(LoginRequest req) {
        Optional<Farmer> maybe = farmerRepository.findByEmail(req.getEmail());
        if (maybe.isEmpty()) return Optional.empty();
        Farmer f = maybe.get();
        if (!passwordEncoder.matches(req.getPassword(), f.getPasswordHash())) return Optional.empty();
        String token = jwtUtil.generateToken(String.valueOf(f.getId()));
        return Optional.of(token);
    }

    // other methods...
}
