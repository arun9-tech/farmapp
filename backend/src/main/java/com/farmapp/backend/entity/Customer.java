package com.farmapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    @JsonIgnore
    private String passwordHash;

    private String address;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getPasswordHash() { return passwordHash; }
    public String getAddress() { return address; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setAddress(String address) { this.address = address; }
}
