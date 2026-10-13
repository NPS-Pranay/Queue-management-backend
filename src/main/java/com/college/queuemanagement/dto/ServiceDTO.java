package com.college.queuemanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class ServiceDTO {

    private Long id;

    @NotBlank(message = "Service name is required")
    private String name;

    private String description;

    private Boolean active = true;

    public ServiceDTO() {
    }

    public ServiceDTO(Long id, String name, String description, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
