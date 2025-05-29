package com.example.dreamcatcherapi.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String email;
    private String password;

    public UserRegistrationRequest() {
    }

    public UserRegistrationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
