package com.college.queuemanagement.service;

import com.college.queuemanagement.dto.ServiceQueueDTO;
import com.college.queuemanagement.dto.TokenDTO;
import com.college.queuemanagement.entity.Counter;
import com.college.queuemanagement.entity.Service;
import com.college.queuemanagement.entity.Token;
import com.college.queuemanagement.repository.CounterRepository;
import com.college.queuemanagement.repository.ServiceRepository;
import com.college.queuemanagement.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class StaffQueueService {

    private final TokenRepository tokenRepository;
    private final ServiceRepository serviceRepository;
    private final CounterRepository counterRepository;

    @Autowired
    public StaffQueueService(TokenRepository tokenRepository, ServiceRepository serviceRepository, CounterRepository counterRepository) {
        this.tokenRepository = tokenRepository;
        this.serviceRepository = serviceRepository;
        this.counterRepository = counterRepository;
    }

    public ServiceQueueDTO getStaffQueue(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + serviceId));

        Optional<Token> servingTokenOpt = tokenRepository.findFirstByServiceIdAndStatus(serviceId, "SERVING");
        Long currentlyServingTokenId = servingTokenOpt.map(Token::getId).orElse(null);
        Integer currentlyServingTokenNumber = servingTokenOpt.map(Token::getTokenNumber).orElse(null);

        List<Token> waitingTokens = tokenRepository.findByServiceIdAndStatus(serviceId, "WAITING");
        List<TokenDTO> waitingDTOs = waitingTokens.stream().map(this::mapToDTO).toList();

        return new ServiceQueueDTO(
            service.getId(),
            service.getName(),
            currentlyServingTokenId,
            currentlyServingTokenNumber,
            waitingDTOs.size(),
            waitingDTOs
        );
    }

    public TokenDTO callNextToken(Long serviceId, Long counterId) {
        Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + serviceId));

        // Auto-complete any existing SERVING token for this service to ensure only 1 token is SERVING at a time
        Optional<Token> currentServingOpt = tokenRepository.findFirstByServiceIdAndStatus(serviceId, "SERVING");
        currentServingOpt.ifPresent(token -> {
            token.setStatus("COMPLETED");
            tokenRepository.save(token);
        });

        // Find the next WAITING token (first in line by token number)
        Optional<Token> nextTokenOpt = tokenRepository.findFirstByServiceIdAndStatusOrderByTokenNumberAsc(serviceId, "WAITING");

        if (nextTokenOpt.isEmpty()) {
            TokenDTO emptyDto = new TokenDTO();
            emptyDto.setServiceId(serviceId);
            emptyDto.setServiceName(service.getName());
            emptyDto.setMessage("No waiting tokens in queue for this service");
            return emptyDto;
        }

        Token nextToken = nextTokenOpt.get();
        nextToken.setStatus("SERVING");

        if (counterId != null) {
            Optional<Counter> counterOpt = counterRepository.findById(counterId);
            counterOpt.ifPresent(nextToken::setCounter);
        }

        Token updatedToken = tokenRepository.save(nextToken);
        TokenDTO dto = mapToDTO(updatedToken);
        dto.setMessage("Token T-" + updatedToken.getTokenNumber() + " is now SERVING");
        return dto;
    }

    public Optional<TokenDTO> completeToken(Long id) {
        return tokenRepository.findById(id).map(token -> {
            if ("SERVING".equals(token.getStatus())) {
                token.setStatus("COMPLETED");
                Token updated = tokenRepository.save(token);
                TokenDTO dto = mapToDTO(updated);
                dto.setMessage("Token completed successfully");
                return dto;
            } else {
                TokenDTO dto = mapToDTO(token);
                dto.setMessage("Only SERVING tokens can be marked as COMPLETED");
                return dto;
            }
        });
    }

    public Optional<TokenDTO> skipToken(Long id) {
        return tokenRepository.findById(id).map(token -> {
            if ("WAITING".equals(token.getStatus()) || "SERVING".equals(token.getStatus())) {
                token.setStatus("SKIPPED");
                Token updated = tokenRepository.save(token);
                TokenDTO dto = mapToDTO(updated);
                dto.setMessage("Token skipped successfully");
                return dto;
            } else {
                TokenDTO dto = mapToDTO(token);
                dto.setMessage("Token cannot be skipped from current status: " + token.getStatus());
                return dto;
            }
        });
    }

    private TokenDTO mapToDTO(Token token) {
        long peopleAhead = 0;
        if ("WAITING".equals(token.getStatus())) {
            peopleAhead = tokenRepository.countByServiceIdAndStatusAndTokenNumberLessThan(
                token.getService().getId(), "WAITING", token.getTokenNumber()
            );
        }

        Integer currentServingTokenNumber = tokenRepository.findFirstByServiceIdAndStatus(
            token.getService().getId(), "SERVING"
        ).map(Token::getTokenNumber).orElse(null);

        return new TokenDTO(
            token.getId(),
            token.getTokenNumber(),
            token.getUser().getId(),
            token.getUser().getName(),
            token.getService().getId(),
            token.getService().getName(),
            token.getCounter() != null ? token.getCounter().getId() : null,
            token.getCounter() != null ? token.getCounter().getName() : null,
            token.getStatus(),
            token.getCreatedAt(),
            peopleAhead,
            currentServingTokenNumber,
            null
        );
    }
}
