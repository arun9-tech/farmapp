package com.farmapp.backend.dto;

public class CustomerLoginRequest {

    private String mobile;
    private String password;

    public String getMobile() { return mobile; }
    public String getPassword() { return password; }

    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setPassword(String password) { this.password = password; }
}
