package com.example.ExpenseTrackr.expenseTrackr.user.service;

import com.example.ExpenseTrackr.expenseTrackr.security.dto.RegisterRequest;
import com.example.ExpenseTrackr.expenseTrackr.security.entity.VerificationCode;
import com.example.ExpenseTrackr.expenseTrackr.security.repository.VerificationCodeRepository;
import com.example.ExpenseTrackr.expenseTrackr.security.service.EmailService;

import com.example.ExpenseTrackr.expenseTrackr.user.entity.User;
import com.example.ExpenseTrackr.expenseTrackr.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       VerificationCodeRepository verificationCodeRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
    }


    public void register(RegisterRequest request) throws Exception {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new Exception("Username already exists");
        }

        // Generate verification code
        String code = generateVerificationCode();

        // Save verification code to the database
        VerificationCode verificationCode = new VerificationCode(request.getEmail(),
                code, request.getUsername(), request.getPassword());
        verificationCodeRepository.save(verificationCode);

        // Send verification email
        emailService.sendVerificationEmail(request.getEmail(), code);
    }

    public void confirmEmail(String email, String code) throws Exception {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByEmail(email);
        if (optionalCode.isPresent() && optionalCode.get().getCode().equals(code)) {
            VerificationCode verificationCode = optionalCode.get();
            User user = new User();
            user.setUsername(verificationCode.getUsername());
            user.setEmail(verificationCode.getEmail());
            user.setPassword(passwordEncoder.encode(verificationCode.getPassword()));
            userRepository.save(user);
            verificationCodeRepository.delete(verificationCode);
        } else {
            throw new Exception("Invalid verification code");
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public Optional<User> authenticate(String login, String password) {
        try {
            Optional<User> user = userRepository.findByEmail(login)
                    .or(() -> userRepository.findByUsername(login));

            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                return user;
            } else {
                log.info("Authentication failed for user: {}", login);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Authentication failed for user: " + login, e);
            return Optional.empty();
        }
    }

    public void sendPasswordResetCode(String email) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            String code = generateVerificationCode();
            VerificationCode verificationCode = new VerificationCode(email, code,
                    userOptional.get().getUsername(), userOptional.get().getPassword());
            verificationCodeRepository.save(verificationCode);
            emailService.sendVerificationEmail(email, code);
        } else {
            throw new Exception("Email not found");
        }
    }

    public void resetPassword(String email, String code, String newPassword) throws Exception {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByEmail(email);
        if (optionalCode.isPresent() && optionalCode.get().getCode().equals(code)) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found"));
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            verificationCodeRepository.delete(optionalCode.get());
        } else {
            throw new Exception("Invalid verification code");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: "
                        + username));
    }

    public void updateCurrency(String email, String selectedCurrency) {
        try {
            log.info("Updating currency for email: {}", email);
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setSelectedCurrency(selectedCurrency);
                userRepository.save(user);
                log.info("Currency updated to {} for user {}", selectedCurrency, email);
            } else {
                log.error("User with email {} not found", email);
                throw new Exception("User not found");
            }
        } catch (Exception e) {
            log.error("Error updating currency for email {}: {}", email, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateBalance(String email, double balance) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBalance(balance);
            userRepository.save(user);
        } else {
            throw new Exception("User not found");
        }
    }

    public Double getBalanceByEmail(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getBalance();
        }
        throw new Exception("User not found with email: " + email);
    }

    public String getCurrencyByEmail(String email) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getSelectedCurrency();
        }
        throw new Exception("User not found with email: " + email);
    }
}
