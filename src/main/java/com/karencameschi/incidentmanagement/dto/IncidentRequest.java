package com.karencameschi.incidentmanagement.dto;

import com.karencameschi.incidentmanagement.entity.IncidentPriority;
import com.karencameschi.incidentmanagement.entity.IncidentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IncidentRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 120, message = "Title must have at most 120 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must have at most 1000 characters")
    private String description;

    @NotNull(message = "Status is required")
    private IncidentStatus status;

    @NotNull(message = "Priority is required")
    private IncidentPriority priority;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public IncidentPriority getPriority() {
        return priority;
    }

    public void setPriority(IncidentPriority priority) {
        this.priority = priority;
    }
}
