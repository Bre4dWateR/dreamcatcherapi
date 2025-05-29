package com.example.dreamcatcherapi.service;

import com.example.dreamcatcherapi.entity.User;
import com.example.dreamcatcherapi.dto.UserRegistrationRequest; // Добавлен импорт
import java.util.List;
import java.util.Optional;

public interface UserService {
    // Изменена сигнатура метода registerUser для приема DTO
    User registerUser(UserRegistrationRequest request);

    Optional<User> authenticateUser(String email, String password);
    Optional<User> findById(Long id);
    List<User> findAllUsers();
    User updateUser(User user);
    void deleteUser(Long userId);
    User changeUserEmail(Long userId, String newEmail, String currentPassword);
    User changeUserPassword(Long userId, String oldPassword, String newPassword);
    Optional<User> findByEmail(String email);
}
