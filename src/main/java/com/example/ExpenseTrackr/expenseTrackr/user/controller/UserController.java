package com.example.ExpenseTrackr.expenseTrackr.user.controller;

import com.example.ExpenseTrackr.expenseTrackr.user.dto.BalanceUpdateRequest;
import com.example.ExpenseTrackr.expenseTrackr.user.dto.CurrencyUpdateRequest;
import com.example.ExpenseTrackr.expenseTrackr.user.service.UserService;
import com.example.ExpenseTrackr.expenseTrackr.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user.getBalance());
    }

    @PatchMapping("/updateCurrency")
    public ResponseEntity<String> updateCurrency(@RequestBody CurrencyUpdateRequest request) {
        try {
            userService.updateCurrency(request.getEmail(), request.getSelectedCurrency());
            return ResponseEntity.ok("Currency updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update currency: " + e.getMessage());
        }
    }

    @PatchMapping("/updateBalance")
    public ResponseEntity<Map<String, String>> updateBalance(@RequestBody BalanceUpdateRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.updateBalance(request.getEmail(), request.getBalance());
            response.put("success", "true");
            response.put("message", "Balance updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Failed to update balance: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/balanceByEmail")
    public ResponseEntity<Double> getBalanceByEmail(@RequestParam String email) {
        try {
            Double balance = userService.getBalanceByEmail(email);
            if (balance != null) {
                return ResponseEntity.ok(balance);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/currencyByEmail")
    public ResponseEntity<String> getCurrencyByEmail(@RequestParam String email) {
        try {
            String currency = userService.getCurrencyByEmail(email);
            if (currency != null) {
                return ResponseEntity.ok(currency);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}