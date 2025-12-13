package com.farmapp.backend.service;

import com.farmapp.backend.model.Farmer;
import com.farmapp.backend.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FarmerService {
    private final FarmerRepository farmerRepository;

    public Farmer save(Farmer f) {
        return farmerRepository.save(f);
    }

    public Optional<Farmer> findByEmail(String email) {
        return farmerRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return farmerRepository.existsByEmail(email);
    }

    public Optional<Farmer> findById(Long id) {
        return farmerRepository.findById(id);
    }
}
