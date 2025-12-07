package com.emrullah.phoneNumberVerify.Response;

import com.emrullah.phoneNumberVerify.User;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRegisterResDTO {
    private String status;
    private String message;
    private User data;
    private boolean isValid;

    public UserRegisterResDTO() {
    }

    public UserRegisterResDTO(String status, String message, boolean isValid) {
        this.status = status;
        this.message = message;
        this.isValid = isValid;
    }

    public UserRegisterResDTO(String status, String message, User data, boolean isValid) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
