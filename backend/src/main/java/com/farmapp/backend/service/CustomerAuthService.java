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

    public void signup(CustomerSignupRequest req) {

        if (customerRepository.existsByEmail(req.email)) {
            throw new RuntimeException("Email already registered");
        }

        Customer c = new Customer();
        c.setName(req.name);
        c.setEmail(req.email);
        c.setMobile(req.mobile);
        c.setAddress(req.address);
        c.setPasswordHash(passwordEncoder.encode(req.password));

        customerRepository.save(c);
    }

    public Customer login(CustomerLoginRequest req) {

        Customer customer = customerRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!passwordEncoder.matches(req.getPassword(), customer.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return customer;
    }

}
