package com.karencameschi.incidentmanagement.controller;

import com.karencameschi.incidentmanagement.entity.Incident;
import com.karencameschi.incidentmanagement.service.IncidentService;
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
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public List<Incident> findAll() {
        return incidentService.findAll();
    }

    @GetMapping("/{id}")
    public Incident findById(@PathVariable Long id) {
        return incidentService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Incident> create(@RequestBody Incident incident) {
        Incident createdIncident = incidentService.create(incident);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdIncident);
    }

    @PutMapping("/{id}")
    public Incident update(
            @PathVariable Long id,
            @RequestBody Incident incident) {

        return incidentService.update(id, incident);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        incidentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}