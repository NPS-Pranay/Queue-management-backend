package com.college.queuemanagement.service;

import com.college.queuemanagement.dto.AuthResponse;
import com.college.queuemanagement.dto.LoginRequest;
import com.college.queuemanagement.dto.RegisterRequest;
import com.college.queuemanagement.entity.User;
import com.college.queuemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse registerUser(RegisterRequest request) {
        // Validation check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email is already registered. Please use another email.", "ERROR");
        }

        // Determine user role (defaults to 'USER')
        String role = "USER";
        if (request.getRole() != null && !request.getRole().trim().isEmpty()) {
            String upperRole = request.getRole().trim().toUpperCase();
            if (upperRole.equals("ADMIN") || upperRole.equals("STAFF") || upperRole.equals("USER")) {
                role = upperRole;
            }
        }

        // Encrypt password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Create and save User
        User newUser = new User(request.getName(), request.getEmail(), encodedPassword, role);
        User savedUser = userRepository.save(newUser);

        return new AuthResponse(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getEmail(),
            savedUser.getRole(),
            "Registration successful",
            "SUCCESS"
        );
    }

    public AuthResponse loginUser(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return new AuthResponse("Invalid email or password", "ERROR");
        }

        User user = userOptional.get();

        // Verify hashed password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid email or password", "ERROR");
        }

        return new AuthResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            "Login successful",
            "SUCCESS"
        );
    }
}
