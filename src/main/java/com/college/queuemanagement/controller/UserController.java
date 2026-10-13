package com.college.queuemanagement.controller;

import com.college.queuemanagement.dto.AuthResponse;
import com.college.queuemanagement.dto.TokenDTO;
import com.college.queuemanagement.entity.User;
import com.college.queuemanagement.repository.UserRepository;
import com.college.queuemanagement.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{userId}/tokens")
    public ResponseEntity<List<TokenDTO>> getUserTokens(@PathVariable Long userId) {
        List<TokenDTO> tokens = tokenService.getUserTokens(userId);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping
    public ResponseEntity<List<AuthResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<AuthResponse> dtos = users.stream().map(u -> new AuthResponse(
            u.getId(),
            u.getName(),
            u.getEmail(),
            u.getRole(),
            "User retrieved successfully",
            "SUCCESS"
        )).toList();
        return ResponseEntity.ok(dtos);
    }
}
