package com.example.ExpenseTrackr.expenseTrackr.transaction.controller;

import com.example.ExpenseTrackr.expenseTrackr.transaction.dto.*;
import com.example.ExpenseTrackr.expenseTrackr.transaction.entity.Category;
import com.example.ExpenseTrackr.expenseTrackr.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactional")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/expenses")
    public ResponseEntity<ExpenseResponse> addExpense(@RequestBody ExpenseRequest expenseRequest) {
        ExpenseResponse response = transactionService.addExpense(expenseRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/incomes")
    public ResponseEntity<IncomeResponse> addIncome(@RequestBody IncomeRequest incomeRequest) {
        IncomeResponse response = transactionService.addIncome(incomeRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getExpenses")
    public ResponseEntity<List<ExpenseListResponse>> getExpenses(@RequestBody FilterRequest filterRequest) {
        List<ExpenseListResponse> response = transactionService.getExpenses(filterRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getIncomes")
    public ResponseEntity<List<IncomeListResponse>> getIncomes(@RequestBody FilterRequest filterRequest) {
        List<IncomeListResponse> response = transactionService.getIncomes(filterRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCategories")
    public ResponseEntity<List<Category>> getCategoriesByType(@RequestParam String type) {
        List<Category> categories = transactionService.getCategoriesByType(type);
        return ResponseEntity.ok(categories);
    }

}
