package com.college.queuemanagement.service;

import com.college.queuemanagement.dto.ServiceDTO;
import com.college.queuemanagement.entity.Service;
import com.college.queuemanagement.repository.CounterRepository;
import com.college.queuemanagement.repository.ServiceRepository;
import com.college.queuemanagement.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceManagementService {

    private final ServiceRepository serviceRepository;
    private final CounterRepository counterRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public ServiceManagementService(ServiceRepository serviceRepository, CounterRepository counterRepository, TokenRepository tokenRepository) {
        this.serviceRepository = serviceRepository;
        this.counterRepository = counterRepository;
        this.tokenRepository = tokenRepository;
    }

    public ServiceDTO createService(ServiceDTO dto) {
        Service service = new Service(
            dto.getName(),
            dto.getDescription(),
            dto.getActive() != null ? dto.getActive() : true
        );
        Service saved = serviceRepository.save(service);
        return mapToDTO(saved);
    }

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream()
            .map(this::mapToDTO)
            .toList();
    }

    public Optional<ServiceDTO> getServiceById(Long id) {
        return serviceRepository.findById(id)
            .map(this::mapToDTO);
    }

    public Optional<ServiceDTO> updateService(Long id, ServiceDTO dto) {
        return serviceRepository.findById(id).map(existingService -> {
            existingService.setName(dto.getName());
            existingService.setDescription(dto.getDescription());
            if (dto.getActive() != null) {
                existingService.setActive(dto.getActive());
            }
            Service updated = serviceRepository.save(existingService);
            return mapToDTO(updated);
        });
    }

    @Transactional
    public boolean deleteService(Long id) {
        if (serviceRepository.existsById(id)) {
            // Delete child tokens and counters to ensure clean cascading
            tokenRepository.findByServiceId(id).forEach(token -> tokenRepository.deleteById(token.getId()));
            counterRepository.findByServiceId(id).forEach(counter -> counterRepository.deleteById(counter.getId()));
            serviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ServiceDTO mapToDTO(Service service) {
        return new ServiceDTO(
            service.getId(),
            service.getName(),
            service.getDescription(),
            service.getActive()
        );
    }
}
