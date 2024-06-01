package com.example.ExpenseTrackr.expenseTrackr.transaction.repository;

import com.example.ExpenseTrackr.expenseTrackr.transaction.entity.Expense;
import com.example.ExpenseTrackr.expenseTrackr.transaction.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Income> findByUserId(Long id);

    Optional<Income> findByUserIdAndCategoryNameAndDate(Long userId, String categoryName, LocalDate date);

}
