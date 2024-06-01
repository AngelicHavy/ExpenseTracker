package com.example.ExpenseTrackr.expenseTrackr.transaction.dto;

import java.time.LocalDate;

public class FilterRequest {
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
