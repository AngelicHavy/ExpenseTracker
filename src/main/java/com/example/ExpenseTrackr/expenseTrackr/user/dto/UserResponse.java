package com.example.ExpenseTrackr.expenseTrackr.user.dto;

import com.example.ExpenseTrackr.expenseTrackr.user.entity.User;

public class UserResponse {
    private String username;
    private String email;
    private String currency;
    private Double balance;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.currency = user.getSelectedCurrency();
        this.balance = user.getBalance();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
