package com.example.dreamcatcherapi.controller;

import com.example.dreamcatcherapi.entity.User;
import com.example.dreamcatcherapi.service.UserService;
import com.example.dreamcatcherapi.dto.UserRegistrationRequest;
import com.example.dreamcatcherapi.dto.ChangeEmailRequest;
import com.example.dreamcatcherapi.dto.ChangePasswordRequest;
import com.example.dreamcatcherapi.dto.LoginRequest;

import com.example.dreamcatcherapi.exception.EmailAlreadyTakenException;
import com.example.dreamcatcherapi.exception.IncorrectPasswordException;
import com.example.dreamcatcherapi.exception.NewPasswordSameAsOldException;
import com.example.dreamcatcherapi.exception.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        if (registrationRequest.getEmail() == null || registrationRequest.getEmail().trim().isEmpty() ||
                registrationRequest.getPassword() == null || registrationRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email and password cannot be empty.");
        }

        try {
            User registeredUser = userService.registerUser(registrationRequest); // Pass the DTO directly
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (EmailAlreadyTakenException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during registration.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email and password cannot be empty.");
        }

        Optional<User> authenticatedUser = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (authenticatedUser.isPresent()) {
            return new ResponseEntity<>(authenticatedUser.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (user.getId() == null || !id.equals(user.getId())) {
            return ResponseEntity.badRequest().body("User ID in path and body do not match or is missing.");
        }
        try {
            User updatedUser = userService.updateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EmailAlreadyTakenException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during user update.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during account deletion.");
        }
    }

    @PutMapping("/{id}/change-email")
    public ResponseEntity<?> changeEmail(@PathVariable Long id, @RequestBody ChangeEmailRequest request) {
        if (request.getNewEmail() == null || request.getNewEmail().trim().isEmpty() ||
                request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("New email and current password cannot be empty.");
        }
        try {
            User updatedUser = userService.changeUserEmail(id, request.getNewEmail(), request.getCurrentPassword());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (EmailAlreadyTakenException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during email change.");
        }
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        if (request.getOldPassword() == null || request.getOldPassword().trim().isEmpty() ||
                request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Old and new passwords cannot be empty.");
        }
        try {
            User updatedUser = userService.changeUserPassword(id, request.getOldPassword(), request.getNewPassword());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NewPasswordSameAsOldException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during password change.");
        }
    }
}

