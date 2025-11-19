package com.helpysoft.mima_api.repository;

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
public interface CommercialShipRepository extends JpaRepository<CommercialShips, Long> {

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.trackingId = :trackingId")
    Optional<CommercialShips> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.imoNumber = :imoNumber")
    Optional<CommercialShips> findByImoNumber(@Param("imoNumber") String imoNumber);

    @Query("SELECT cs FROM CommercialShips cs WHERE LOWER(cs.shipName) LIKE LOWER(CONCAT('%', :shipName, '%'))")
    List<CommercialShips> findByShipNameContainingIgnoreCase(@Param("shipName") String shipName);

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.shipType = :shipType")
    List<CommercialShips> findByShipType(@Param("shipType") String shipType);

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.status = :status")
    List<CommercialShips> findByStatus(@Param("status") String status);

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.isActive = true")
    List<CommercialShips> findByIsActiveTrue();

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.flag = :flag")
    List<CommercialShips> findByFlag(@Param("flag") String flag);

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.arrivalDate BETWEEN :startDate AND :endDate")
    List<CommercialShips> findByArrivalDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT cs FROM CommercialShips cs WHERE cs.status = :status AND cs.arrivalDate >= :date")
    List<CommercialShips> findCurrentlyInPort(@Param("status") String status, @Param("date") LocalDateTime date);

    @Query("SELECT COUNT(cs) FROM CommercialShips cs WHERE cs.isActive = true")
    long countActiveShips();

    @Query("SELECT cs FROM CommercialShips cs WHERE " +
            "LOWER(cs.shipName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cs.imoNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cs.shipType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cs.flag) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cs.mmsi) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cs.callSign) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cs.shipOwner) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(cs.operator) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<CommercialShips> searchCommercialShips(@Param("searchTerm") String searchTerm);
}
