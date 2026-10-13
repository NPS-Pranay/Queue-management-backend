package com.college.queuemanagement.controller;

import com.college.queuemanagement.dto.TokenDTO;
import com.college.queuemanagement.dto.TokenPositionDTO;
import com.college.queuemanagement.dto.TokenRequest;
import com.college.queuemanagement.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
@CrossOrigin(origins = "*")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> createToken(@Valid @RequestBody TokenRequest request) {
        TokenDTO created = tokenService.createToken(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TokenDTO> getTokenById(@PathVariable Long id) {
        return tokenService.getTokenById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/position")
    public ResponseEntity<TokenPositionDTO> getTokenPosition(@PathVariable Long id) {
        return tokenService.getTokenPosition(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TokenDTO>> getUserTokens(@PathVariable Long userId) {
        List<TokenDTO> tokens = tokenService.getUserTokens(userId);
        return ResponseEntity.ok(tokens);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TokenDTO> cancelToken(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId) {
        return tokenService.cancelToken(id, userId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
