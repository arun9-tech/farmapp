package com.farmapp.backend.controller;

import com.farmapp.backend.dto.*;
import com.farmapp.backend.entity.Customer;
import com.farmapp.backend.entity.Farmer;
import com.farmapp.backend.service.AuthService;
import com.farmapp.backend.service.CustomerAuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CustomerAuthService customerAuthService;

    public AuthController(AuthService authService,
                          CustomerAuthService customerAuthService) {
        this.authService = authService;
        this.customerAuthService = customerAuthService;
    }

    // FARMER
    @PostMapping("/farmer/signup")
    public String farmerSignup(@RequestBody FarmerSignupRequest req) {
        authService.signup(req);
        return "Farmer registered successfully";
    }

    @PostMapping("/farmer/login")
    public Farmer farmerLogin(@RequestBody FarmerLoginRequest req) {
        return authService.login(req);
    }

    // CUSTOMER
    @PostMapping("/customer/signup")
    public String customerSignup(@RequestBody CustomerSignupRequest req) {
        customerAuthService.signup(req);
        return "Customer registered successfully";
    }

    @PostMapping("/customer/login")
    public Customer customerLogin(@RequestBody CustomerLoginRequest req) {
        return customerAuthService.login(req);
    }
}
