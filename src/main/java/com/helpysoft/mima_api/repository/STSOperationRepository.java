package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.STSOperation;
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
public interface STSOperationRepository extends JpaRepository<STSOperation, Long> {

    Optional<STSOperation> findByTrackingId(UUID trackingId);

    Optional<STSOperation> findByOperationNumber(String operationNumber);

    List<STSOperation> findByMotherVessel(CommercialShips motherVessel);

    List<STSOperation> findByReceivingVessel(CommercialShips receivingVessel);

    @Query("SELECT s FROM STSOperation s WHERE s.motherVessel = :ship OR s.receivingVessel = :ship ORDER BY s.startDate DESC")
    List<STSOperation> findByShip(@Param("ship") CommercialShips ship);

    List<STSOperation> findByStatus(String status);

    @Query("SELECT s FROM STSOperation s WHERE s.startDate BETWEEN :startDate AND :endDate")
    List<STSOperation> findByStartDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Opérations STS en cours
    @Query("SELECT s FROM STSOperation s WHERE s.status = 'EN_COURS' ORDER BY s.startDate DESC")
    List<STSOperation> findOngoingOperations();

    // Opérations avec pollution
    @Query("SELECT s FROM STSOperation s WHERE s.pollutionOccurred = true ORDER BY s.startDate DESC")
    List<STSOperation> findOperationsWithPollution();

    // Statistiques par type de cargaison
    @Query("SELECT s.cargoType, COUNT(s), SUM(s.quantityTransferred) FROM STSOperation s GROUP BY s.cargoType")
    List<Object[]> statisticsByCargoType();

    // Statistiques par opérateur
    @Query("SELECT s.stsOperator, COUNT(s) FROM STSOperation s GROUP BY s.stsOperator")
    List<Object[]> countByOperator();

    // Statistiques par zone maritime
    @Query("SELECT s.maritimeZone, COUNT(s) FROM STSOperation s GROUP BY s.maritimeZone")
    List<Object[]> countByMaritimeZone();
}
