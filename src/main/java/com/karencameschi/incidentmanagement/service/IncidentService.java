package com.karencameschi.incidentmanagement.service;

import com.karencameschi.incidentmanagement.entity.Incident;
import com.karencameschi.incidentmanagement.exception.IncidentNotFoundException;
import com.karencameschi.incidentmanagement.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public List<Incident> findAll() {
        return incidentRepository.findAll();
    }

    public Incident findById(Long id) {
    return incidentRepository.findById(id)
            .orElseThrow(() -> new IncidentNotFoundException(id));
    }

    public Incident create(Incident incident) {
        return incidentRepository.save(incident);
    }

    public Incident update(Long id, Incident updatedIncident) {
        Incident existingIncident = findById(id);

        existingIncident.setTitle(updatedIncident.getTitle());
        existingIncident.setDescription(updatedIncident.getDescription());
        existingIncident.setStatus(updatedIncident.getStatus());
        existingIncident.setPriority(updatedIncident.getPriority());

        return incidentRepository.save(existingIncident);
    }

    public void delete(Long id) {
        Incident incident = findById(id);
        incidentRepository.delete(incident);
    }
}

