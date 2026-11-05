package com.karencameschi.incidentmanagement.controller;

import com.karencameschi.incidentmanagement.entity.Incident;
import com.karencameschi.incidentmanagement.entity.IncidentPriority;
import com.karencameschi.incidentmanagement.entity.IncidentStatus;
import com.karencameschi.incidentmanagement.exception.IncidentNotFoundException;
import com.karencameschi.incidentmanagement.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

@WebMvcTest(IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IncidentService incidentService;

    @Test
    void shouldReturnIncidentWhenIdExists() throws Exception {
        Incident incident = new Incident(
                "API unavailable",
                "Authentication service returned an error",
                IncidentStatus.OPEN,
                IncidentPriority.HIGH);

        when(incidentService.findById(1L))
                .thenReturn(incident);

        mockMvc.perform(get("/incidents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("API unavailable"))
                .andExpect(jsonPath("$.description")
                        .value("Authentication service returned an error"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void shouldReturnNotFoundWhenIncidentDoesNotExist() throws Exception {
        when(incidentService.findById(999L))
                .thenThrow(new IncidentNotFoundException(999L));

        mockMvc.perform(get("/incidents/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title")
                        .value("Incident not found"))
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.detail")
                        .value("Incident not found with id: 999"));
    }

    @Test
    void shouldCreateIncidentWhenRequestIsValid() throws Exception {
        Incident createdIncident = new Incident(
                "API unavailable",
                "Authentication service returned an error",
                IncidentStatus.OPEN,
                IncidentPriority.HIGH);

        when(incidentService.create(any(Incident.class)))
                .thenReturn(createdIncident);

        String requestBody = """
                {
                  "title": "API unavailable",
                  "description": "Authentication service returned an error",
                  "status": "OPEN",
                  "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("API unavailable"))
                .andExpect(jsonPath("$.description")
                        .value("Authentication service returned an error"))
                .andExpect(jsonPath("$.status")
                        .value("OPEN"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
        String requestBody = """
                {
                  "title": "",
                  "description": "",
                  "status": null,
                  "priority": null
                }
                """;

        mockMvc.perform(post("/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Invalid request"))
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.detail")
                        .value("One or more fields are invalid"))
                .andExpect(jsonPath("$.errors.title")
                        .value("Title is required"))
                .andExpect(jsonPath("$.errors.description")
                        .value("Description is required"))
                .andExpect(jsonPath("$.errors.status")
                        .value("Status is required"))
                .andExpect(jsonPath("$.errors.priority")
                        .value("Priority is required"));

        verifyNoInteractions(incidentService);
    }

    @Test
    void shouldReturnAllIncidents() throws Exception {
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

        when(incidentService.findAll())
                .thenReturn(List.of(incident1, incident2));

        mockMvc.perform(get("/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title")
                        .value("API unavailable"))
                .andExpect(jsonPath("$[0].status")
                        .value("OPEN"))
                .andExpect(jsonPath("$[1].title")
                        .value("Database connection failure"))
                .andExpect(jsonPath("$[1].priority")
                        .value("CRITICAL"));
    }

    @Test
    void shouldDeleteIncident() throws Exception {
        mockMvc.perform(delete("/incidents/1"))
                .andExpect(status().isNoContent());

        verify(incidentService).delete(1L);
    }

    @Test
    void shouldUpdateIncidentWhenRequestIsValid() throws Exception {
        Incident updatedIncident = new Incident(
                "Updated API incident",
                "Authentication service is being restored",
                IncidentStatus.IN_PROGRESS,
                IncidentPriority.HIGH);

        when(incidentService.update(
                eq(1L),
                any(Incident.class))).thenReturn(updatedIncident);

        String requestBody = """
                {
                  "title": "Updated API incident",
                  "description": "Authentication service is being restored",
                  "status": "IN_PROGRESS",
                  "priority": "HIGH"
                }
                """;

        mockMvc.perform(put("/incidents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Updated API incident"))
                .andExpect(jsonPath("$.description")
                        .value("Authentication service is being restored"))
                .andExpect(jsonPath("$.status")
                        .value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"));

        verify(incidentService).update(
                eq(1L),
                any(Incident.class));
    }
}
