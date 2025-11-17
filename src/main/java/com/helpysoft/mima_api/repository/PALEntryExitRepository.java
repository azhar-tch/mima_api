package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.PALEntryExit;
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
public interface PALEntryExitRepository extends JpaRepository<PALEntryExit, Long> {

    @Query("SELECT p FROM PALEntryExit p WHERE p.trackingId = :trackingId")
    Optional<PALEntryExit> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT p FROM PALEntryExit p WHERE p.commercialShip = :commercialShip")
    List<PALEntryExit> findByCommercialShip(@Param("commercialShip") CommercialShips commercialShip);

    @Query("SELECT p FROM PALEntryExit p WHERE p.commercialShip = :commercialShip ORDER BY p.entryDate DESC")
    List<PALEntryExit> findByCommercialShipOrderByEntryDateDesc(@Param("commercialShip") CommercialShips commercialShip);

    @Query("SELECT p FROM PALEntryExit p WHERE p.entryDate BETWEEN :startDate AND :endDate")
    List<PALEntryExit> findByEntryDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT p FROM PALEntryExit p WHERE p.exitDate BETWEEN :startDate AND :endDate")
    List<PALEntryExit> findByExitDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Navires actuellement au PAL (entrés mais pas encore sortis)
    @Query("SELECT p FROM PALEntryExit p WHERE p.exitDate IS NULL ORDER BY p.entryDate DESC")
    List<PALEntryExit> findShipsCurrentlyInPAL();

    // Statistiques par zone d'ancrage
    @Query("SELECT p.anchorageZone, COUNT(p) FROM PALEntryExit p GROUP BY p.anchorageZone")
    List<Object[]> countByAnchorageZone();

    // Recherche par trackingId du navire commercial
    @Query("SELECT p FROM PALEntryExit p WHERE p.commercialShip.trackingId = :trackingId")
    List<PALEntryExit> findByCommercialShipTrackingId(@Param("trackingId") UUID trackingId);

    // Recherche par zone d'ancrage
    @Query("SELECT p FROM PALEntryExit p WHERE p.anchorageZone = :anchorageZone")
    List<PALEntryExit> findByAnchorageZone(@Param("anchorageZone") String anchorageZone);

    // Recherche par numéro de poste d'amarrage (berth number)
    // Note: Cette méthode nécessite que le champ berthNumber existe dans l'entité PALEntryExit
    @Query("SELECT p FROM PALEntryExit p WHERE p.berthNumber = :berthNumber")
    List<PALEntryExit> findByBerthNumber(@Param("berthNumber") String berthNumber);
}
