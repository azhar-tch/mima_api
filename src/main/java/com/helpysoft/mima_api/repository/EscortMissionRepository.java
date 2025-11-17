package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.EscortMissions;
import com.helpysoft.mima_api.entity.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EscortMissionRepository extends JpaRepository<EscortMissions, Long> {

    Optional<EscortMissions> findByTrackingId(UUID trackingId);

    Optional<EscortMissions> findByMissionNumber(String missionNumber);

    List<EscortMissions> findByStatus(MissionStatus status);

    @Query("SELECT em FROM EscortMissionss em WHERE em.startDate BETWEEN :startDate AND :endDate")
    List<EscortMissions> findByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT em FROM EscortMissionss em WHERE em.commercialShip.id = :shipId")
    List<EscortMissions> findByCommercialShipId(@Param("shipId") Long shipId);

    @Query("SELECT em FROM EscortMissionss em WHERE em.securityAgency.id = :agencyId")
    List<EscortMissions> findBySecurityAgencyId(@Param("agencyId") Long agencyId);

    @Query("SELECT em FROM EscortMissionss em WHERE em.navalVessel.id = :vesselId")
    List<EscortMissions> findByNavalVesselId(@Param("vesselId") Long vesselId);

    @Query("SELECT em FROM EscortMissionss em WHERE em.commander.id = :commanderId")
    List<EscortMissions> findByCommanderId(@Param("commanderId") Long commanderId);

    @Query("SELECT COUNT(em) FROM EscortMissionss em WHERE em.status = :status")
    long countByStatus(@Param("status") MissionStatus status);

    @Query("SELECT COUNT(em) FROM EscortMissionss em WHERE em.startDate >= :date")
    long countSince(@Param("date") LocalDateTime date);
}
