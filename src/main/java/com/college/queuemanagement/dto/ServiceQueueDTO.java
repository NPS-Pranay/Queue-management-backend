package com.college.queuemanagement.dto;

import java.util.List;

public class ServiceQueueDTO {

    private Long serviceId;
    private String serviceName;
    private Long currentlyServingTokenId;
    private Integer currentlyServingTokenNumber;
    private long totalWaiting;
    private List<TokenDTO> waitingTokens;

    public ServiceQueueDTO() {
    }

    public ServiceQueueDTO(Long serviceId, String serviceName, Long currentlyServingTokenId, Integer currentlyServingTokenNumber, long totalWaiting, List<TokenDTO> waitingTokens) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.currentlyServingTokenId = currentlyServingTokenId;
        this.currentlyServingTokenNumber = currentlyServingTokenNumber;
        this.totalWaiting = totalWaiting;
        this.waitingTokens = waitingTokens;
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

    public Long getCurrentlyServingTokenId() {
        return currentlyServingTokenId;
    }

    public void setCurrentlyServingTokenId(Long currentlyServingTokenId) {
        this.currentlyServingTokenId = currentlyServingTokenId;
    }

    public Integer getCurrentlyServingTokenNumber() {
        return currentlyServingTokenNumber;
    }

    public void setCurrentlyServingTokenNumber(Integer currentlyServingTokenNumber) {
        this.currentlyServingTokenNumber = currentlyServingTokenNumber;
    }

    public long getTotalWaiting() {
        return totalWaiting;
    }

    public void setTotalWaiting(long totalWaiting) {
        this.totalWaiting = totalWaiting;
    }

    public List<TokenDTO> getWaitingTokens() {
        return waitingTokens;
    }

    public void setWaitingTokens(List<TokenDTO> waitingTokens) {
        this.waitingTokens = waitingTokens;
    }
}
