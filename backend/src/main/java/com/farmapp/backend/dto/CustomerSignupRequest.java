package com.farmapp.backend.dto;
public class CustomerSignupRequest {

    private String name;
    private String email;
    private String mobile;
    private String password;
    private String address;

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(String address) { this.address = address; }
}
