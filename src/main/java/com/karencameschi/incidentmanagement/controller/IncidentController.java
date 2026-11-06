package com.karencameschi.incidentmanagement.controller;

import com.karencameschi.incidentmanagement.dto.IncidentRequest;
import com.karencameschi.incidentmanagement.entity.Incident;
import com.karencameschi.incidentmanagement.service.IncidentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/incidents")
@Tag(name = "Incident Management", description = "Operations for creating, retrieving, updating and deleting IT incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @Operation(summary = "List all incidents", description = "Returns all registered IT incidents")
    @ApiResponse(responseCode = "200", description = "Incidents retrieved successfully")
    @GetMapping
    public List<Incident> findAll() {
        return incidentService.findAll();
    }

    @Operation(summary = "Find incident by ID", description = "Returns a specific incident using its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incident retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Incident not found")
    })
    @GetMapping("/{id}")
    public Incident findById(@PathVariable Long id) {
        return incidentService.findById(id);
    }

    @Operation(summary = "Create a new incident", description = "Creates a new IT incident using the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Incident created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<Incident> create(
            @Valid @RequestBody IncidentRequest request) {

        Incident incident = new Incident(
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getPriority());

        Incident createdIncident = incidentService.create(incident);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdIncident);
    }

    @Operation(summary = "Update an incident", description = "Updates an existing incident identified by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incident updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Incident not found")
    })
    @PutMapping("/{id}")
    public Incident update(
            @PathVariable Long id,
            @Valid @RequestBody IncidentRequest request) {

        Incident incident = new Incident(
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getPriority());

        return incidentService.update(id, incident);
    }

    @Operation(summary = "Delete an incident", description = "Deletes an existing incident identified by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Incident deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Incident not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incidentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
