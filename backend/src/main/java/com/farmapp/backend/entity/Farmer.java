package com.farmapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "farmers")
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String mobile;

    @JsonIgnore
    private String passwordHash;

    private String farmName;
    private String location;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getPasswordHash() { return passwordHash; }
    public String getFarmName() { return farmName; }
    public String getLocation() { return location; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFarmName(String farmName) { this.farmName = farmName; }
    public void setLocation(String location) { this.location = location; }
}
