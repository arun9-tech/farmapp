package com.farmapp.backend.controller;

import com.farmapp.backend.dto.*;
import com.farmapp.backend.entity.Customer;
import com.farmapp.backend.entity.Farmer;
import com.farmapp.backend.security.JwtUtil;
import com.farmapp.backend.service.AuthService;
import com.farmapp.backend.service.CustomerAuthService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CustomerAuthService customerAuthService;
    private final JwtUtil jwtUtil;

    // ✅ SINGLE CONSTRUCTOR (THIS IS VERY IMPORTANT)
    public AuthController(AuthService authService,
                          CustomerAuthService customerAuthService,
                          JwtUtil jwtUtil) {
        this.authService = authService;
        this.customerAuthService = customerAuthService;
        this.jwtUtil = jwtUtil;
    }

    // ========== FARMER ==========

    @PostMapping("/farmer/signup")
    public String farmerSignup(@RequestBody FarmerSignupRequest req) {
        authService.signup(req);
        return "Farmer registered successfully";
    }

    @PostMapping("/farmer/login")
    public Map<String, Object> farmerLogin(@RequestBody FarmerLoginRequest req) {

        Farmer farmer = authService.login(req);

        String token = jwtUtil.generateToken(farmer.getId(), "FARMER");

        Map<String, Object> res = new HashMap<>();
        res.put("id", farmer.getId());
        res.put("name", farmer.getName());
        res.put("mobile", farmer.getMobile());
        res.put("farmName", farmer.getFarmName());
        res.put("location", farmer.getLocation());
        res.put("token", token);

        return res;
    }

    // ========== CUSTOMER ==========

    @PostMapping("/customer/signup")
    public String customerSignup(@RequestBody CustomerSignupRequest req) {
        customerAuthService.signup(req);
        return "Customer registered successfully";
    }

    @PostMapping("/customer/login")
    public Map<String, Object> customerLogin(@RequestBody CustomerLoginRequest req) {

        Customer customer = customerAuthService.login(req);

        String token = jwtUtil.generateToken(customer.getId(), "CUSTOMER");

        Map<String, Object> res = new HashMap<>();
        res.put("id", customer.getId());
        res.put("name", customer.getName());
        res.put("email", customer.getEmail());
        res.put("mobile", customer.getMobile());
        res.put("address", customer.getAddress());
        res.put("token", token);

        return res;
    }
}
