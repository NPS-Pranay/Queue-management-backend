package com.college.queuemanagement.controller;

import com.college.queuemanagement.dto.ServiceQueueDTO;
import com.college.queuemanagement.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queues")
@CrossOrigin(origins = "*")
public class QueueController {

    private final TokenService tokenService;

    @Autowired
    public QueueController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceQueueDTO> getServiceQueue(@PathVariable Long serviceId) {
        ServiceQueueDTO queue = tokenService.getServiceQueue(serviceId);
        return ResponseEntity.ok(queue);
    }
}
