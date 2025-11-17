package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.CommercialShips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommercialShipRepository extends JpaRepository<CommercialShips, Long> {

    Optional<CommercialShips> findByTrackingId(UUID trackingId);

    Optional<CommercialShips> findByImoNumber(String imoNumber);

    List<CommercialShips> findByShipNameContainingIgnoreCase(String shipName);

    List<CommercialShips> findByShipType(String shipType);

    List<CommercialShips> findByStatus(String status);

    List<CommercialShips> findByIsActiveTrue();

    List<CommercialShips> findByFlag(String flag);

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.arrivalDate BETWEEN :startDate AND :endDate")
    List<CommercialShips> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.status = :status AND cs.arrivalDate >= :date")
    List<CommercialShips> findCurrentlyInPort(String status, LocalDateTime date);

    @Query("SELECT COUNT(cs) FROM CommercialShips cs WHERE cs.isActive = true")
    long countActiveShips();
}
