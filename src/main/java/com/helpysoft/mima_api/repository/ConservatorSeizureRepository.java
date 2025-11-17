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

    Optional<ConservatorSeizure> findByTrackingId(UUID trackingId);

    List<ConservatorSeizure> findByCommercialShip(CommercialShips commercialShip);

    List<ConservatorSeizure> findByCommercialShipOrderBySeizureDateDesc(CommercialShips commercialShip);

    List<ConservatorSeizure> findByStatus(String status);

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
