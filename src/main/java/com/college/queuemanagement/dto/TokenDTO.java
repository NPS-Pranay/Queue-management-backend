package com.college.queuemanagement.dto;

import java.time.LocalDateTime;

public class TokenDTO {

    private Long id;
    private Integer tokenNumber;
    private Long userId;
    private String userName;
    private Long serviceId;
    private String serviceName;
    private Long counterId;
    private String counterName;
    private String status;
    private LocalDateTime createdAt;
    private long peopleAhead;
    private Integer currentServingTokenNumber;
    private String message;

    public TokenDTO() {
    }

    public TokenDTO(Long id, Integer tokenNumber, Long userId, String userName, Long serviceId, String serviceName, Long counterId, String counterName, String status, LocalDateTime createdAt, long peopleAhead, Integer currentServingTokenNumber, String message) {
        this.id = id;
        this.tokenNumber = tokenNumber;
        this.userId = userId;
        this.userName = userName;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.counterId = counterId;
        this.counterName = counterName;
        this.status = status;
        this.createdAt = createdAt;
        this.peopleAhead = peopleAhead;
        this.currentServingTokenNumber = currentServingTokenNumber;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getPeopleAhead() {
        return peopleAhead;
    }

    public void setPeopleAhead(long peopleAhead) {
        this.peopleAhead = peopleAhead;
    }

    public Integer getCurrentServingTokenNumber() {
        return currentServingTokenNumber;
    }

    public void setCurrentServingTokenNumber(Integer currentServingTokenNumber) {
        this.currentServingTokenNumber = currentServingTokenNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
