package com.emrullah.phoneNumberVerify.Response;

import com.emrullah.phoneNumberVerify.User;

import java.util.HashMap;
import java.util.Map;

public class PhoneVerifyResDTO {
    private String number;
    private Map<String , Boolean> rules = new HashMap<>();
    private Boolean isValid;




    public PhoneVerifyResDTO() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Map<String, Boolean> getRules() {
        return rules;
    }

    public void setRules(Map<String, Boolean> rules) {
        this.rules = rules;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
