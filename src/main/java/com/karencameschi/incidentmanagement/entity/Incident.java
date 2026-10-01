package com.karencameschi.incidentmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentPriority priority;


public Incident() {
}

public Incident(
        String title,
        String description,
        IncidentStatus status,
        IncidentPriority priority) {

    this.title = title;
    this.description = description;
    this.status = status;
    this.priority = priority;
}

public Long getId() {
    return id;
}

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
