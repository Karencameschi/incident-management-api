package com.karencameschi.incidentmanagement.repository;

import com.karencameschi.incidentmanagement.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
}
