package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ShipArrivalDeparture;
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
public interface ShipArrivalDepartureRepository extends JpaRepository<ShipArrivalDeparture, Long> {

    @Query("SELECT a FROM ShipArrivalDeparture a WHERE a.trackingId = :trackingId")
    Optional<ShipArrivalDeparture> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT a FROM ShipArrivalDeparture a WHERE a.commercialShip = :commercialShip")
    List<ShipArrivalDeparture> findByCommercialShip(@Param("commercialShip") CommercialShips commercialShip);

    @Query("SELECT a FROM ShipArrivalDeparture a WHERE a.commercialShip = :commercialShip ORDER BY a.arrivalDate DESC")
    List<ShipArrivalDeparture> findByCommercialShipOrderByArrivalDateDesc(@Param("commercialShip") CommercialShips commercialShip);

    @Query("SELECT a FROM ShipArrivalDeparture a WHERE a.arrivalDate BETWEEN :startDate AND :endDate")
    List<ShipArrivalDeparture> findByArrivalDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT a FROM ShipArrivalDeparture a WHERE a.departureDate BETWEEN :startDate AND :endDate")
    List<ShipArrivalDeparture> findByDepartureDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Navires actuellement au port (arrivés mais pas encore partis)
    @Query("SELECT a FROM ShipArrivalDeparture a WHERE a.departureDate IS NULL ORDER BY a.arrivalDate DESC")
    List<ShipArrivalDeparture> findShipsCurrentlyInPort();

    // Statistiques par port de provenance
    @Query("SELECT a.portOfOrigin, COUNT(a) FROM ShipArrivalDeparture a GROUP BY a.portOfOrigin")
    List<Object[]> countByPortOfOrigin();
}
