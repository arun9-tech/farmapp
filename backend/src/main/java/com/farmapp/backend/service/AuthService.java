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

    public void signup(FarmerSignupRequest req) {

    System.out.println("FARMER SIGNUP REQUEST => " + req);

    if (req.getPassword() == null) {
        throw new RuntimeException("Password is NULL from frontend");
    }

    Farmer farmer = new Farmer();
    farmer.setName(req.getName());
    farmer.setMobile(req.getMobile());
    farmer.setFarmName(req.getFarmName());
    farmer.setLocation(req.getLocation());

    farmer.setPasswordHash(
            passwordEncoder.encode(req.getPassword())
    );

    farmerRepository.save(farmer);
}


    // ============ FARMER LOGIN ============
    public Farmer login(FarmerLoginRequest req) {

        Farmer farmer = farmerRepository.findByMobile(req.getMobile())
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        if (!passwordEncoder.matches(req.getPassword(), farmer.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return farmer;
    }
}
