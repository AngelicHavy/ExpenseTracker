package com.example.ExpenseTrackr.expenseTrackr.transaction.dto;

public class ExpenseListResponse {
    private String categoryImage;
    private String categoryName;
    private double percentage;
    private double amount;
    private String currency;

    public ExpenseListResponse(String categoryImage, String categoryName, double percentage, double amount, String currency) {
        this.categoryImage = categoryImage;
        this.categoryName = categoryName;
        this.percentage = percentage;
        this.amount = amount;
        this.currency = currency;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
