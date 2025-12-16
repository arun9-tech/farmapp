package com.farmapp.backend.repository;

import com.farmapp.backend.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    Optional<Farmer> findByMobile(String mobile);

    boolean existsByMobile(String mobile);
}
