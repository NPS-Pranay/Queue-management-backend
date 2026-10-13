package com.college.queuemanagement.service;

import com.college.queuemanagement.dto.ServiceQueueDTO;
import com.college.queuemanagement.dto.TokenDTO;
import com.college.queuemanagement.dto.TokenPositionDTO;
import com.college.queuemanagement.dto.TokenRequest;
import com.college.queuemanagement.entity.Service;
import com.college.queuemanagement.entity.Token;
import com.college.queuemanagement.entity.User;
import com.college.queuemanagement.repository.CounterRepository;
import com.college.queuemanagement.repository.ServiceRepository;
import com.college.queuemanagement.repository.TokenRepository;
import com.college.queuemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final CounterRepository counterRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserRepository userRepository, ServiceRepository serviceRepository, CounterRepository counterRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.counterRepository = counterRepository;
    }

    public TokenDTO createToken(TokenRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUserId()));

        Service service = serviceRepository.findById(request.getServiceId())
            .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + request.getServiceId()));

        if (Boolean.FALSE.equals(service.getActive())) {
            throw new IllegalStateException("Selected service is currently inactive");
        }

        // Prevent duplicate active tokens for the same service
        List<Token> existingActive = tokenRepository.findByUserIdAndServiceIdAndStatusIn(
            request.getUserId(), request.getServiceId(), List.of("WAITING", "SERVING")
        );
        if (!existingActive.isEmpty()) {
            Token activeToken = existingActive.get(0);
            TokenDTO dto = mapToDTO(activeToken);
            dto.setMessage("You already have an active token for this service");
            return dto;
        }

        // Calculate next token number for this specific service
        Optional<Token> lastToken = tokenRepository.findTopByServiceIdOrderByTokenNumberDesc(request.getServiceId());
        int nextTokenNumber = lastToken.map(t -> t.getTokenNumber() + 1).orElse(1);

        Token token = new Token(nextTokenNumber, user, service, null, "WAITING");
        Token savedToken = tokenRepository.save(token);

        TokenDTO dto = mapToDTO(savedToken);
        dto.setMessage("Token generated successfully");
        return dto;
    }

    public Optional<TokenDTO> getTokenById(Long id) {
        return tokenRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<TokenPositionDTO> getTokenPosition(Long id) {
        return tokenRepository.findById(id).map(token -> {
            long peopleAhead = 0;
            if ("WAITING".equals(token.getStatus())) {
                peopleAhead = tokenRepository.countByServiceIdAndStatusAndTokenNumberLessThan(
                    token.getService().getId(), "WAITING", token.getTokenNumber()
                );
            }

            Integer currentServingTokenNumber = tokenRepository.findFirstByServiceIdAndStatus(
                token.getService().getId(), "SERVING"
            ).map(Token::getTokenNumber).orElse(null);

            String formattedNumber = "T-" + token.getTokenNumber();

            return new TokenPositionDTO(
                token.getId(),
                token.getTokenNumber(),
                formattedNumber,
                token.getService().getId(),
                token.getService().getName(),
                currentServingTokenNumber,
                peopleAhead,
                token.getStatus()
            );
        });
    }

    public ServiceQueueDTO getServiceQueue(Long serviceId) {
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

    public List<TokenDTO> getUserTokens(Long userId) {
        return tokenRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(this::mapToDTO)
            .toList();
    }

    public Optional<TokenDTO> cancelToken(Long id, Long userId) {
        return tokenRepository.findById(id).map(token -> {
            // Validate user ownership if userId parameter is provided
            if (userId != null && !token.getUser().getId().equals(userId)) {
                TokenDTO dto = mapToDTO(token);
                dto.setMessage("Unauthorized: You can only cancel your own token");
                return dto;
            }

            if ("WAITING".equals(token.getStatus())) {
                token.setStatus("CANCELLED");
                Token updated = tokenRepository.save(token);
                TokenDTO dto = mapToDTO(updated);
                dto.setMessage("Token cancelled successfully");
                return dto;
            } else {
                TokenDTO dto = mapToDTO(token);
                dto.setMessage("Token in '" + token.getStatus() + "' status cannot be cancelled");
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
