package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.CommercialShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommercialShipRepository extends JpaRepository<CommercialShip, Long> {

    Optional<CommercialShip> findByTrackingId(UUID trackingId);

    Optional<CommercialShip> findByImoNumber(String imoNumber);

    List<CommercialShip> findByShipNameContainingIgnoreCase(String shipName);

    List<CommercialShip> findByShipType(String shipType);

    List<CommercialShip> findByStatus(String status);

    List<CommercialShip> findByIsActiveTrue();

    List<CommercialShip> findByFlag(String flag);

    @Query("SELECT cs FROM CommercialShip cs WHERE cs.arrivalDate BETWEEN :startDate AND :endDate")
    List<CommercialShip> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT cs FROM CommercialShip cs WHERE cs.status = :status AND cs.arrivalDate >= :date")
    List<CommercialShip> findCurrentlyInPort(String status, LocalDateTime date);

    @Query("SELECT COUNT(cs) FROM CommercialShip cs WHERE cs.isActive = true")
    long countActiveShips();
}
