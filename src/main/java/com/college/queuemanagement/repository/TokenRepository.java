package com.college.queuemanagement.repository;

import com.college.queuemanagement.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUserId(Long userId);
    List<Token> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Token> findByServiceIdAndStatus(Long serviceId, String status);
    List<Token> findByServiceId(Long serviceId);
    Optional<Token> findTopByServiceIdOrderByTokenNumberDesc(Long serviceId);

    // Active token check for same user and service
    List<Token> findByUserIdAndServiceIdAndStatusIn(Long userId, Long serviceId, List<String> statuses);

    // Calculate number of people waiting ahead
    long countByServiceIdAndStatusAndTokenNumberLessThan(Long serviceId, String status, Integer tokenNumber);

    // Find current serving token for a service
    Optional<Token> findFirstByServiceIdAndStatus(Long serviceId, String status);

    // Find next waiting token for staff to call
    Optional<Token> findFirstByServiceIdAndStatusOrderByTokenNumberAsc(Long serviceId, String status);
}
