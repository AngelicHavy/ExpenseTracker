package com.example.ExpenseTrackr.expenseTrackr.security.repository;

import com.example.ExpenseTrackr.expenseTrackr.security.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmail(String email);
}
