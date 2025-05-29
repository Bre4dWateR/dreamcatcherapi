package com.example.dreamcatcherapi.service;

import com.example.dreamcatcherapi.entity.User;
import com.example.dreamcatcherapi.repository.UserRepository;
import com.example.dreamcatcherapi.exception.UserNotFoundException;
import com.example.dreamcatcherapi.exception.EmailAlreadyTakenException;
import com.example.dreamcatcherapi.exception.IncorrectPasswordException;
import com.example.dreamcatcherapi.exception.NewPasswordSameAsOldException;
import com.example.dreamcatcherapi.dto.UserRegistrationRequest; // Import the DTO
import org.springframework.security.crypto.password.PasswordEncoder; // Un-comment or add this line
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Un-comment this line

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) { // Add PasswordEncoder to parameters
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // Un-comment this line
    }

    @Override
    public User registerUser(UserRegistrationRequest request) { // Changed to accept DTO
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("User with this email already exists.");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Password HASHING
        return userRepository.save(user);
    }

    @Override
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) { // Compare HASHED passwords
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + user.getId()));

        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new EmailAlreadyTakenException("Email '" + user.getEmail() + "' is already taken.");
            }
            existingUser.setEmail(user.getEmail());
        }

        // Removed username update logic as it's no longer in the User entity
        // if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
        //     existingUser.setUsername(user.getUsername());
        // }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User changeUserEmail(Long userId, String newEmail, String currentPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) { // Compare HASHED passwords
            throw new IncorrectPasswordException("Incorrect current password.");
        }

        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailAlreadyTakenException("Email '" + newEmail + "' is already taken.");
        }

        user.setEmail(newEmail);
        // Removed setting username, as it's no longer in the User entity
        // user.setUsername(newEmail);
        return userRepository.save(user);
    }

    @Override
    public User changeUserPassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) { // Compare HASHED passwords
            throw new IncorrectPasswordException("Incorrect old password.");
        }

        // It's still good practice to check if new password is same as old, but compare against hashed
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new NewPasswordSameAsOldException("New password cannot be the same as the old password.");
        }

        user.setPassword(passwordEncoder.encode(newPassword)); // HASH the new password
        return userRepository.save(user);
    }
}