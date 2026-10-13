package com.college.queuemanagement.dto;

public class TokenPositionDTO {

    private Long tokenId;
    private Integer tokenNumber;
    private String formattedTokenNumber;
    private Long serviceId;
    private String serviceName;
    private Integer currentlyServingTokenNumber;
    private long peopleAhead;
    private String status;

    public TokenPositionDTO() {
    }

    public TokenPositionDTO(Long tokenId, Integer tokenNumber, String formattedTokenNumber, Long serviceId, String serviceName, Integer currentlyServingTokenNumber, long peopleAhead, String status) {
        this.tokenId = tokenId;
        this.tokenNumber = tokenNumber;
        this.formattedTokenNumber = formattedTokenNumber;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.currentlyServingTokenNumber = currentlyServingTokenNumber;
        this.peopleAhead = peopleAhead;
        this.status = status;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Integer getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(Integer tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getFormattedTokenNumber() {
        return formattedTokenNumber;
    }

    public void setFormattedTokenNumber(String formattedTokenNumber) {
        this.formattedTokenNumber = formattedTokenNumber;
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

    public Integer getCurrentlyServingTokenNumber() {
        return currentlyServingTokenNumber;
    }

    public void setCurrentlyServingTokenNumber(Integer currentlyServingTokenNumber) {
        this.currentlyServingTokenNumber = currentlyServingTokenNumber;
    }

    public long getPeopleAhead() {
        return peopleAhead;
    }

    public void setPeopleAhead(long peopleAhead) {
        this.peopleAhead = peopleAhead;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
