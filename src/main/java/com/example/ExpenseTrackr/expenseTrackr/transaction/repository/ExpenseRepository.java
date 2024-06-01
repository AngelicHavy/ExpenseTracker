package com.example.ExpenseTrackr.expenseTrackr.transaction.repository;

import com.example.ExpenseTrackr.expenseTrackr.transaction.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserId(Long id);

    Optional<Expense> findByUserIdAndCategoryNameAndDate(Long userId, String categoryName, LocalDate date);
}
