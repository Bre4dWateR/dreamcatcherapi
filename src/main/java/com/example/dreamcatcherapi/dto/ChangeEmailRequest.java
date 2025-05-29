package com.example.dreamcatcherapi.dto;

// import lombok.Data; // Раскомментируйте, если используете Lombok

// @Data // Если используете Lombok, это автоматически сгенерирует геттеры и сеттеры
public class ChangeEmailRequest {
    private String newEmail;
    private String currentPassword;

    public ChangeEmailRequest() {
    }

    public ChangeEmailRequest(String newEmail, String currentPassword) {
        this.newEmail = newEmail;
        this.currentPassword = currentPassword;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}