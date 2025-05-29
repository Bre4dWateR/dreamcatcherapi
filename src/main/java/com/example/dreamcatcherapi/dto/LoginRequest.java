package com.example.dreamcatcherapi.dto;

// import lombok.Data; // Раскомментируйте, если используете Lombok

// @Data // Если используете Lombok, это автоматически сгенерирует геттеры и сеттеры
public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
