package com.example.ExpenseTrackr.expenseTrackr.transaction.dto;

public class ExpenseResponse {
    private Double amount;
    private String categoryName;
    private String categoryImage;
    private String date;
    private Double percentage;
    private Double newBalance;

    public ExpenseResponse(Double amount, String categoryName, String categoryImage, String date, Double percentage, Double newBalance) {
        this.amount = amount;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.date = date;
        this.percentage = percentage;
        this.newBalance = newBalance;
    }

    // Getters and setters
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Double newBalance) {
        this.newBalance = newBalance;
    }
}
