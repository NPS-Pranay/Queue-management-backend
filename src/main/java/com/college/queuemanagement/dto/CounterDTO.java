package com.college.queuemanagement.dto;

public class CounterDTO {

    private Long id;
    private String name;
    private Long serviceId;
    private String serviceName;
    private Boolean active;

    public CounterDTO() {
    }

    public CounterDTO(Long id, String name, Long serviceId, String serviceName, Boolean active) {
        this.id = id;
        this.name = name;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
