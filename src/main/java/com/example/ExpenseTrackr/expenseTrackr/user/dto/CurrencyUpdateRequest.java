package com.example.ExpenseTrackr.expenseTrackr.user.dto;

public class CurrencyUpdateRequest {
    private String email;
    private String selectedCurrency;

    public CurrencyUpdateRequest() {
    }

    public CurrencyUpdateRequest(String email, String selectedCurrency) {
        this.email = email;
        this.selectedCurrency = selectedCurrency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }
}