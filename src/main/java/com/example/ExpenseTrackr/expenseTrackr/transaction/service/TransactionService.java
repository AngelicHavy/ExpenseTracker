package com.example.ExpenseTrackr.expenseTrackr.transaction.service;

import com.example.ExpenseTrackr.expenseTrackr.transaction.dto.*;
import com.example.ExpenseTrackr.expenseTrackr.transaction.entity.Category;
import com.example.ExpenseTrackr.expenseTrackr.transaction.entity.Expense;
import com.example.ExpenseTrackr.expenseTrackr.transaction.entity.Income;
import com.example.ExpenseTrackr.expenseTrackr.transaction.repository.CategoryRepository;
import com.example.ExpenseTrackr.expenseTrackr.transaction.repository.ExpenseRepository;
import com.example.ExpenseTrackr.expenseTrackr.transaction.repository.IncomeRepository;
import com.example.ExpenseTrackr.expenseTrackr.user.entity.User;
import com.example.ExpenseTrackr.expenseTrackr.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public List<ExpenseListResponse> getExpenses(FilterRequest filterRequest) {
        logger.info("Fetching expenses for user: {}", filterRequest.getEmail());
        Optional<User> user = userRepository.findByEmail(filterRequest.getEmail());
        if (user.isPresent()) {
            User foundUser = user.get();
            LocalDate startDate = filterRequest.getStartDate();
            LocalDate endDate = filterRequest.getEndDate();

            List<Expense> expenses = expenseRepository.findByUserIdAndDateBetween(
                    foundUser.getId(),
                    startDate,
                    endDate
            );

            double totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();

            return expenses.stream()
                    .map(expense -> new ExpenseListResponse(
                            expense.getCategory().getIconUrl(),
                            expense.getCategory().getName(),
                            Math.round((expense.getAmount() / totalAmount) * 100),
                            expense.getAmount(),
                            foundUser.getSelectedCurrency()
                    ))
                    .collect(Collectors.toList());
        }
        logger.error("User not found: {}", filterRequest.getEmail());
        throw new UsernameNotFoundException("User not found");
    }

    public List<IncomeListResponse> getIncomes(FilterRequest filterRequest) {
        logger.info("Fetching incomes for user: {}", filterRequest.getEmail());
        Optional<User> user = userRepository.findByEmail(filterRequest.getEmail());
        if (user.isPresent()) {
            User foundUser = user.get();
            LocalDate startDate = filterRequest.getStartDate();
            LocalDate endDate = filterRequest.getEndDate();

            List<Income> incomes = incomeRepository.findByUserIdAndDateBetween(
                    foundUser.getId(),
                    startDate,
                    endDate
            );

            double totalAmount = incomes.stream().mapToDouble(Income::getAmount).sum();

            return incomes.stream()
                    .map(income -> new IncomeListResponse(
                            income.getCategory().getIconUrl(),
                            income.getCategory().getName(),
                            Math.round((income.getAmount() / totalAmount) * 100),
                            income.getAmount(),
                            foundUser.getSelectedCurrency()
                    ))
                    .collect(Collectors.toList());
        }
        logger.error("User not found: {}", filterRequest.getEmail());
        throw new UsernameNotFoundException("User not found");
    }

    @Transactional
    public ExpenseResponse addExpense(ExpenseRequest expenseRequest) {
        Optional<User> userOptional = userRepository.findByEmail(expenseRequest.getEmail());
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOptional.get();

        Optional<Category> categoryOptional = categoryRepository.findByName(expenseRequest.getCategoryName())
                .filter(cat -> cat.getType().equals("expense"));
        if (!categoryOptional.isPresent()) {
            throw new IllegalArgumentException("Category not found or not an expense");
        }

        Category category = categoryOptional.get();
        LocalDate today = LocalDate.now();

        // Check if there is an existing expense for the same user, category, and date
        Optional<Expense> existingExpenseOptional = expenseRepository.findByUserIdAndCategoryNameAndDate(
                user.getId(), category.getName(), today
        );

        Expense expense;
        if (existingExpenseOptional.isPresent()) {
            expense = existingExpenseOptional.get();
            expense.setAmount(expense.getAmount() + expenseRequest.getAmount());
        } else {
            expense = new Expense();
            expense.setAmount(expenseRequest.getAmount());
            expense.setCategory(category);
            expense.setDate(today);
            expense.setUser(user);
            expenseRepository.save(expense);
        }

        // Update the user's balance
        double newBalance = user.getBalance() - expenseRequest.getAmount();
        user.setBalance(newBalance);
        userRepository.save(user);

        // Calculate the percentage of the expense
        List<Expense> allExpenses = expenseRepository.findByUserId(user.getId());
        double totalExpenses = allExpenses.stream().mapToDouble(Expense::getAmount).sum();
        double percentage = (expense.getAmount() / totalExpenses) * 100;
        percentage = (double) Math.round(percentage);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = expense.getDate().format(formatter);

        ExpenseResponse response = new ExpenseResponse(expense.getAmount(), category.getName(), category.getIconUrl(), date, percentage, newBalance);

        return response;
    }

    public List<Category> getCategoriesByType(String type) {
        return categoryRepository.findByType(type);
    }


    public IncomeResponse addIncome(IncomeRequest incomeRequest) {
        Optional<User> userOptional = userRepository.findByEmail(incomeRequest.getEmail());
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOptional.get();

        Optional<Category> categoryOptional = categoryRepository.findByName(
                        incomeRequest.getCategoryName())
                .filter(cat -> cat.getType().equals("income"));
        if (!categoryOptional.isPresent()) {
            throw new IllegalArgumentException("Category not found or not an income");
        }

        Category category = categoryOptional.get();
        LocalDate today = LocalDate.now();

        Optional<Income> existingIncomeOptional = incomeRepository.findByUserIdAndCategoryNameAndDate(
                user.getId(), category.getName(), today
        );

        Income income;
        if (existingIncomeOptional.isPresent()) {
            income = existingIncomeOptional.get();
            income.setAmount(income.getAmount() + incomeRequest.getAmount());
        } else {
            income = new Income();
            income.setAmount(incomeRequest.getAmount());
            income.setCategory(category);
            income.setDate(today);
            income.setUser(user);
            incomeRepository.save(income);
        }

        // Update the user's balance
        double newBalance = user.getBalance() + incomeRequest.getAmount();
        user.setBalance(newBalance);
        userRepository.save(user);

        // Calculate the percentage of the expense
        List<Income> allIncomes = incomeRepository.findByUserId(user.getId());
        double totalIncomes = allIncomes.stream().mapToDouble(Income::getAmount).sum();
        double percentage = (income.getAmount() / totalIncomes) * 100;
        percentage = (double) Math.round(percentage);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = income.getDate().format(formatter);

        IncomeResponse response = new IncomeResponse(income.getAmount(),
                category.getName(), category.getIconUrl(), date, percentage, newBalance);

        return response;
    }
}
