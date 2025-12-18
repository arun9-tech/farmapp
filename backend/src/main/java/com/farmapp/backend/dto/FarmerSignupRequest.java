package com.farmapp.backend.dto;
public class FarmerSignupRequest {

    private String name;
    private String mobile;
    private String password;
    private String farmName;
    private String location;

    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getPassword() { return password; }
    public String getFarmName() { return farmName; }
    public String getLocation() { return location; }

    public void setName(String name) { this.name = name; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setPassword(String password) { this.password = password; }
    public void setFarmName(String farmName) { this.farmName = farmName; }
    public void setLocation(String location) { this.location = location; }
}
