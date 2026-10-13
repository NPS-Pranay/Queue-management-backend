package com.college.queuemanagement.controller;

import com.college.queuemanagement.dto.CounterDTO;
import com.college.queuemanagement.dto.CounterRequest;
import com.college.queuemanagement.service.CounterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counters")
@CrossOrigin(origins = "*")
public class CounterController {

    private final CounterService counterService;

    @Autowired
    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @PostMapping
    public ResponseEntity<CounterDTO> createCounter(@Valid @RequestBody CounterRequest request) {
        CounterDTO created = counterService.createCounter(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<CounterDTO>> getAllCounters() {
        List<CounterDTO> counters = counterService.getAllCounters();
        return ResponseEntity.ok(counters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CounterDTO> getCounterById(@PathVariable Long id) {
        return counterService.getCounterById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CounterDTO> updateCounter(@PathVariable Long id, @Valid @RequestBody CounterRequest request) {
        return counterService.updateCounter(id, request)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCounter(@PathVariable Long id) {
        boolean deleted = counterService.deleteCounter(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
