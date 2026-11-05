package com.karencameschi.incidentmanagement.service;

import com.karencameschi.incidentmanagement.entity.Incident;
import com.karencameschi.incidentmanagement.entity.IncidentPriority;
import com.karencameschi.incidentmanagement.entity.IncidentStatus;
import com.karencameschi.incidentmanagement.exception.IncidentNotFoundException;
import com.karencameschi.incidentmanagement.repository.IncidentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

        @Mock
        private IncidentRepository incidentRepository;

        @InjectMocks
        private IncidentService incidentService;

        @Test
        void shouldReturnIncidentWhenIdExists() {
                Incident incident = new Incident(
                                "API unavailable",
                                "Authentication service returned an error",
                                IncidentStatus.OPEN,
                                IncidentPriority.HIGH);

                when(incidentRepository.findById(1L))
                                .thenReturn(Optional.of(incident));

                Incident result = incidentService.findById(1L);

                assertSame(incident, result);
        }

        @Test
        void shouldThrowExceptionWhenIncidentDoesNotExist() {
                when(incidentRepository.findById(999L))
                                .thenReturn(Optional.empty());

                IncidentNotFoundException exception = assertThrows(
                                IncidentNotFoundException.class,
                                () -> incidentService.findById(999L));

                assertEquals(
                                "Incident not found with id: 999",
                                exception.getMessage());
        }

        @Test
        void shouldReturnAllIncidents() {
                Incident incident1 = new Incident(
                                "API unavailable",
                                "Authentication service returned an error",
                                IncidentStatus.OPEN,
                                IncidentPriority.HIGH);

                Incident incident2 = new Incident(
                                "Database connection failure",
                                "Application cannot connect to database",
                                IncidentStatus.IN_PROGRESS,
                                IncidentPriority.CRITICAL);

                when(incidentRepository.findAll())
                                .thenReturn(List.of(incident1, incident2));

                List<Incident> result = incidentService.findAll();

                assertEquals(2, result.size());
                assertSame(incident1, result.get(0));
                assertSame(incident2, result.get(1));
        }

        @Test
        void shouldCreateIncident() {
                Incident incident = new Incident(
                                "API unavailable",
                                "Authentication service returned an error",
                                IncidentStatus.OPEN,
                                IncidentPriority.HIGH);

                when(incidentRepository.save(incident))
                                .thenReturn(incident);

                Incident result = incidentService.create(incident);

                assertSame(incident, result);

                verify(incidentRepository).save(incident);
        }

        @Test
        void shouldUpdateIncident() {
                Incident existingIncident = new Incident(
                                "Old title",
                                "Old description",
                                IncidentStatus.OPEN,
                                IncidentPriority.LOW);

                Incident updatedIncident = new Incident(
                                "Updated title",
                                "Updated description",
                                IncidentStatus.IN_PROGRESS,
                                IncidentPriority.HIGH);

                when(incidentRepository.findById(1L))
                                .thenReturn(Optional.of(existingIncident));

                when(incidentRepository.save(existingIncident))
                                .thenReturn(existingIncident);

                Incident result = incidentService.update(1L, updatedIncident);

                assertEquals("Updated title", result.getTitle());
                assertEquals("Updated description", result.getDescription());
                assertEquals(IncidentStatus.IN_PROGRESS, result.getStatus());
                assertEquals(IncidentPriority.HIGH, result.getPriority());

                verify(incidentRepository).save(existingIncident);
        }

        @Test
        void shouldDeleteIncident() {
                Incident incident = new Incident(
                                "API unavailable",
                                "Authentication service returned an error",
                                IncidentStatus.OPEN,
                                IncidentPriority.HIGH);

                when(incidentRepository.findById(1L))
                                .thenReturn(Optional.of(incident));

                incidentService.delete(1L);

                verify(incidentRepository).delete(incident);
        }

}
