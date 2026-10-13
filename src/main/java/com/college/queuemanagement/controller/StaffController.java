package com.college.queuemanagement.controller;

import com.college.queuemanagement.dto.ServiceQueueDTO;
import com.college.queuemanagement.dto.TokenDTO;
import com.college.queuemanagement.service.StaffQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffQueueService staffQueueService;

    @Autowired
    public StaffController(StaffQueueService staffQueueService) {
        this.staffQueueService = staffQueueService;
    }

    @GetMapping("/queue/{serviceId}")
    public ResponseEntity<ServiceQueueDTO> getStaffQueue(@PathVariable Long serviceId) {
        ServiceQueueDTO queue = staffQueueService.getStaffQueue(serviceId);
        return ResponseEntity.ok(queue);
    }

    @PostMapping("/queue/{serviceId}/next")
    public ResponseEntity<TokenDTO> callNextToken(
            @PathVariable Long serviceId,
            @RequestParam(required = false) Long counterId) {
        TokenDTO token = staffQueueService.callNextToken(serviceId, counterId);
        return ResponseEntity.ok(token);
    }

    @PutMapping("/tokens/{id}/complete")
    public ResponseEntity<TokenDTO> completeToken(@PathVariable Long id) {
        return staffQueueService.completeToken(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/tokens/{id}/skip")
    public ResponseEntity<TokenDTO> skipToken(@PathVariable Long id) {
        return staffQueueService.skipToken(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
