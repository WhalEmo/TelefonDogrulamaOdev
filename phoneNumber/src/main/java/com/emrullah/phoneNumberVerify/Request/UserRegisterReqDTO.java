package com.emrullah.phoneNumberVerify.Request;

public class UserRegisterReqDTO {
    private String name;
    private String email;
    private String phone;

    public UserRegisterReqDTO(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
