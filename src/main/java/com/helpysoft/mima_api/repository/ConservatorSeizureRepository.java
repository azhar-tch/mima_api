package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ConservatorSeizure;
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
public interface ConservatorSeizureRepository extends JpaRepository<ConservatorSeizure, Long> {

    @Query("SELECT cs FROM ConservatorSeizure cs WHERE cs.trackingId = :trackingId")
    Optional<ConservatorSeizure> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT cs FROM ConservatorSeizure cs WHERE cs.commercialShip = :commercialShip")
    List<ConservatorSeizure> findByCommercialShip(@Param("commercialShip") CommercialShips commercialShip);

    @Query("SELECT cs FROM ConservatorSeizure cs WHERE cs.commercialShip = :commercialShip ORDER BY cs.seizureDate DESC")
    List<ConservatorSeizure> findByCommercialShipOrderBySeizureDateDesc(@Param("commercialShip") CommercialShips commercialShip);

    @Query("SELECT cs FROM ConservatorSeizure cs WHERE cs.commercialShip.id = :shipId")
    List<ConservatorSeizure> findByCommercialShipId(@Param("shipId") Long shipId);

    // Saisies par trackingId du navire commercial
    @Query("SELECT cs FROM ConservatorSeizure cs WHERE cs.commercialShip.trackingId = :trackingId")
    List<ConservatorSeizure> findByCommercialShipTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT cs FROM ConservatorSeizure cs WHERE cs.status = :status")
    List<ConservatorSeizure> findByStatus(@Param("status") String status);

    @Query("SELECT cs FROM ConservatorSeizure cs WHERE cs.seizingAuthority = :seizingAuthority")
    List<ConservatorSeizure> findBySeizingAuthority(@Param("seizingAuthority") String seizingAuthority);

    @Query("SELECT cs FROM ConservatorSeizure cs WHERE cs.seizureType = :seizureType")
    List<ConservatorSeizure> findBySeizureType(@Param("seizureType") String seizureType);

    @Query("SELECT c FROM ConservatorSeizure c WHERE c.seizureDate BETWEEN :startDate AND :endDate")
    List<ConservatorSeizure> findBySeizureDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Saisies en cours
    @Query("SELECT c FROM ConservatorSeizure c WHERE c.status = 'EN_COURS' ORDER BY c.seizureDate DESC")
    List<ConservatorSeizure> findActiveSeizures();

    // Saisies par autorité saisissante
    @Query("SELECT c.seizingAuthority, COUNT(c) FROM ConservatorSeizure c GROUP BY c.seizingAuthority")
    List<Object[]> countBySeizingAuthority();

    // Saisies par type
    @Query("SELECT c.seizureType, COUNT(c) FROM ConservatorSeizure c GROUP BY c.seizureType")
    List<Object[]> countBySeizureType();
}
