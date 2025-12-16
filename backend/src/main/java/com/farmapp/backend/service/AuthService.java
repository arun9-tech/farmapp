package com.farmapp.backend.service;

import com.farmapp.backend.dto.FarmerLoginRequest;
import com.farmapp.backend.dto.FarmerSignupRequest;
import com.farmapp.backend.entity.Farmer;
import com.farmapp.backend.repository.FarmerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(FarmerRepository farmerRepository,
                       PasswordEncoder passwordEncoder) {
        this.farmerRepository = farmerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ FARMER SIGNUP
    public void signup(FarmerSignupRequest req) {

        if (farmerRepository.existsByMobile(req.mobile)) {
            throw new RuntimeException("Mobile already registered");
        }

        Farmer farmer = new Farmer();
        farmer.setName(req.name);
        farmer.setMobile(req.mobile);
        farmer.setFarmName(req.farmName);
        farmer.setLocation(req.location);
        farmer.setPasswordHash(passwordEncoder.encode(req.password));

        farmerRepository.save(farmer);
    }

    public Farmer login(FarmerLoginRequest req) {

        Farmer farmer = farmerRepository.findByMobile(req.getMobile())
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        if (!passwordEncoder.matches(req.getPassword(), farmer.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return farmer;
    }
}
