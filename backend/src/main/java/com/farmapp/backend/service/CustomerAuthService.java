package com.farmapp.backend.service;

import com.farmapp.backend.dto.CustomerLoginRequest;
import com.farmapp.backend.dto.CustomerSignupRequest;
import com.farmapp.backend.entity.Customer;
import com.farmapp.backend.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerAuthService(CustomerRepository customerRepository,
                               PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= SIGNUP =================
    public void signup(CustomerSignupRequest req) {

        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        if (customerRepository.findByMobile(req.getMobile()).isPresent()) {
            throw new RuntimeException("Mobile already registered");
        }

        Customer customer = new Customer();
        customer.setName(req.getName());
        customer.setEmail(req.getEmail());
        customer.setMobile(req.getMobile());
        customer.setAddress(req.getAddress());

        // ✅ HASH PASSWORD
        customer.setPasswordHash(
                passwordEncoder.encode(req.getPassword())
        );

        customerRepository.save(customer);
    }

    // ================= LOGIN =================
    public Customer login(CustomerLoginRequest req) {

        Customer customer = customerRepository.findByMobile(req.getMobile())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!passwordEncoder.matches(req.getPassword(), customer.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return customer;
    }
}
