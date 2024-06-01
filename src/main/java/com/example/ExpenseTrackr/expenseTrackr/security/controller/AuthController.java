package com.example.ExpenseTrackr.expenseTrackr.security.controller;

import com.example.ExpenseTrackr.expenseTrackr.security.dto.*;
import com.example.ExpenseTrackr.expenseTrackr.user.entity.User;
import com.example.ExpenseTrackr.expenseTrackr.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        Optional<User> user = userService.authenticate(request.getUsername(), request.getPassword());
        Map<String, String> response = new HashMap<>();
        if (user.isPresent()) {
            response.put("success", "true");
            response.put("message", "User logged in successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", "false");
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.register(request);
            response.put("success", "true");
            response.put("message", "User registered successfully. Please check your email for the verification code.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<Map<String, String>> confirmEmail(@RequestBody ConfirmRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.confirmEmail(request.getEmail(), request.getCode());
            response.put("success", "true");
            response.put("message", "Email confirmed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Email confirmation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.sendPasswordResetCode(request.getEmail());
            response.put("success", "true");
            response.put("message", "Password reset code sent to email");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Failed to send password reset code: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
            response.put("success", "true");
            response.put("message", "Password reset successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Failed to reset password: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
