package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.EscortMission;
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
public interface EscortMissionRepository extends JpaRepository<EscortMission, Long> {

    Optional<EscortMission> findByTrackingId(UUID trackingId);

    Optional<EscortMission> findByMissionNumber(String missionNumber);

    List<EscortMission> findByStatus(MissionStatus status);

    @Query("SELECT em FROM EscortMission em WHERE em.startDate BETWEEN :startDate AND :endDate")
    List<EscortMission> findByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT em FROM EscortMission em WHERE em.commercialShip.id = :shipId")
    List<EscortMission> findByCommercialShipId(@Param("shipId") Long shipId);

    @Query("SELECT em FROM EscortMission em WHERE em.securityAgency.id = :agencyId")
    List<EscortMission> findBySecurityAgencyId(@Param("agencyId") Long agencyId);

    @Query("SELECT em FROM EscortMission em WHERE em.navalVessel.id = :vesselId")
    List<EscortMission> findByNavalVesselId(@Param("vesselId") Long vesselId);

    @Query("SELECT em FROM EscortMission em WHERE em.commander.id = :commanderId")
    List<EscortMission> findByCommanderId(@Param("commanderId") Long commanderId);

    @Query("SELECT COUNT(em) FROM EscortMission em WHERE em.status = :status")
    long countByStatus(@Param("status") MissionStatus status);

    @Query("SELECT COUNT(em) FROM EscortMission em WHERE em.startDate >= :date")
    long countSince(@Param("date") LocalDateTime date);
}
