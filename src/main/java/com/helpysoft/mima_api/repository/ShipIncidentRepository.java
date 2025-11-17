package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ShipIncident;
import com.helpysoft.mima_api.entity.CommercialShips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipIncidentRepository extends JpaRepository<ShipIncident, Long> {

    @Query("SELECT i FROM ShipIncident i WHERE i.trackingId = :trackingId")
    Optional<ShipIncident> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT i FROM ShipIncident i WHERE i.commercialShip = :commercialShip")
    List<ShipIncident> findByCommercialShip(@Param("commercialShip") CommercialShips commercialShip);

    @Query("SELECT i FROM ShipIncident i WHERE i.commercialShip = :commercialShip ORDER BY i.incidentDate DESC")
    List<ShipIncident> findByCommercialShipOrderByIncidentDateDesc(@Param("commercialShip") CommercialShips commercialShip);

    @Query("SELECT i FROM ShipIncident i WHERE i.commercialShip.id = :commercialShipId")
    List<ShipIncident> findByCommercialShipId(@Param("commercialShipId") Long commercialShipId);

    @Query("SELECT i FROM ShipIncident i WHERE i.eventType = :eventType")
    List<ShipIncident> findByEventType(@Param("eventType") String eventType);

    @Query("SELECT i FROM ShipIncident i WHERE i.status = :status")
    List<ShipIncident> findByStatus(@Param("status") String status);

    @Query("SELECT i FROM ShipIncident i WHERE i.severity = :severity")
    List<ShipIncident> findBySeverity(@Param("severity") String severity);

    @Query("SELECT i FROM ShipIncident i WHERE i.maritimeZone = :maritimeZone")
    List<ShipIncident> findByMaritimeZone(@Param("maritimeZone") String maritimeZone);

    @Query("SELECT i FROM ShipIncident i WHERE i.incidentDate BETWEEN :startDate AND :endDate")
    List<ShipIncident> findByIncidentDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Incidents en cours
    @Query("SELECT i FROM ShipIncident i WHERE i.status = 'EN_COURS' ORDER BY i.incidentDate DESC")
    List<ShipIncident> findOngoingIncidents();

    // Incidents avec pollution
    @Query("SELECT i FROM ShipIncident i WHERE i.pollutionOccurred = true ORDER BY i.incidentDate DESC")
    List<ShipIncident> findIncidentsWithPollution();

    // Statistiques par type d'incident
    @Query("SELECT i.incidentType, COUNT(i) FROM ShipIncident i GROUP BY i.incidentType")
    List<Object[]> countByIncidentType();

    // Statistiques par gravité
    @Query("SELECT i.severity, COUNT(i) FROM ShipIncident i GROUP BY i.severity")
    List<Object[]> countBySeverity();

    // Incidents par zone maritime
    @Query("SELECT i.maritimeZone, COUNT(i) FROM ShipIncident i GROUP BY i.maritimeZone")
    List<Object[]> countByMaritimeZone();
}
