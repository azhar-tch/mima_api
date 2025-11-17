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

    @Query("SELECT s FROM STSOperation s WHERE s.trackingId = :trackingId")
    Optional<STSOperation> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT s FROM STSOperation s WHERE s.operationNumber = :operationNumber")
    Optional<STSOperation> findByOperationNumber(@Param("operationNumber") String operationNumber);

    @Query("SELECT s FROM STSOperation s WHERE s.motherVessel = :motherVessel")
    List<STSOperation> findByMotherVessel(@Param("motherVessel") CommercialShips motherVessel);

    @Query("SELECT s FROM STSOperation s WHERE s.receivingVessel = :receivingVessel")
    List<STSOperation> findByReceivingVessel(@Param("receivingVessel") CommercialShips receivingVessel);

    @Query("SELECT s FROM STSOperation s WHERE s.motherVessel = :ship OR s.receivingVessel = :ship ORDER BY s.startDate DESC")
    List<STSOperation> findByShip(@Param("ship") CommercialShips ship);

    @Query("SELECT s FROM STSOperation s WHERE s.motherVessel.trackingId = :vesselTrackingId")
    List<STSOperation> findByMotherVesselTrackingId(@Param("vesselTrackingId") UUID vesselTrackingId);

    @Query("SELECT s FROM STSOperation s WHERE s.receivingVessel.trackingId = :vesselTrackingId")
    List<STSOperation> findByReceivingVesselTrackingId(@Param("vesselTrackingId") UUID vesselTrackingId);

    @Query("SELECT s FROM STSOperation s WHERE s.supervisingNavalVessel.trackingId = :vesselTrackingId")
    List<STSOperation> findBySupervisingNavalVesselTrackingId(@Param("vesselTrackingId") UUID vesselTrackingId);

    @Query("SELECT s FROM STSOperation s WHERE s.cargoType = :cargoType")
    List<STSOperation> findByCargoType(@Param("cargoType") String cargoType);

    @Query("SELECT s FROM STSOperation s WHERE s.maritimeZone = :maritimeZone")
    List<STSOperation> findByMaritimeZone(@Param("maritimeZone") String maritimeZone);

    @Query("SELECT s FROM STSOperation s WHERE s.status = :status")
    List<STSOperation> findByStatus(@Param("status") String status);

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
