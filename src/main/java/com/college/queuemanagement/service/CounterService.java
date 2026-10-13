package com.college.queuemanagement.service;

import com.college.queuemanagement.dto.CounterDTO;
import com.college.queuemanagement.dto.CounterRequest;
import com.college.queuemanagement.entity.Counter;
import com.college.queuemanagement.entity.Service;
import com.college.queuemanagement.repository.CounterRepository;
import com.college.queuemanagement.repository.ServiceRepository;
import com.college.queuemanagement.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CounterService {

    private final CounterRepository counterRepository;
    private final ServiceRepository serviceRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public CounterService(CounterRepository counterRepository, ServiceRepository serviceRepository, TokenRepository tokenRepository) {
        this.counterRepository = counterRepository;
        this.serviceRepository = serviceRepository;
        this.tokenRepository = tokenRepository;
    }

    public CounterDTO createCounter(CounterRequest request) {
        Service service = serviceRepository.findById(request.getServiceId())
            .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + request.getServiceId()));

        Counter counter = new Counter(
            request.getName(),
            service,
            request.getActive() != null ? request.getActive() : true
        );
        Counter saved = counterRepository.save(counter);
        return mapToDTO(saved);
    }

    public List<CounterDTO> getAllCounters() {
        return counterRepository.findAll().stream()
            .map(this::mapToDTO)
            .toList();
    }

    public Optional<CounterDTO> getCounterById(Long id) {
        return counterRepository.findById(id)
            .map(this::mapToDTO);
    }

    public Optional<CounterDTO> updateCounter(Long id, CounterRequest request) {
        return counterRepository.findById(id).map(existingCounter -> {
            existingCounter.setName(request.getName());
            if (request.getServiceId() != null) {
                Service service = serviceRepository.findById(request.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + request.getServiceId()));
                existingCounter.setService(service);
            }
            if (request.getActive() != null) {
                existingCounter.setActive(request.getActive());
            }
            Counter updated = counterRepository.save(existingCounter);
            return mapToDTO(updated);
        });
    }

    @Transactional
    public boolean deleteCounter(Long id) {
        if (counterRepository.existsById(id)) {
            // Unlink counter references from tokens before deleting counter
            tokenRepository.findAll().forEach(token -> {
                if (token.getCounter() != null && token.getCounter().getId().equals(id)) {
                    token.setCounter(null);
                    tokenRepository.save(token);
                }
            });
            counterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private CounterDTO mapToDTO(Counter counter) {
        return new CounterDTO(
            counter.getId(),
            counter.getName(),
            counter.getService() != null ? counter.getService().getId() : null,
            counter.getService() != null ? counter.getService().getName() : null,
            counter.getActive()
        );
    }
}
