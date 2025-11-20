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

    @Query("SELECT em FROM EscortMissions em WHERE em.trackingId = :trackingId")
    Optional<EscortMissions> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT em FROM EscortMissions em WHERE em.missionNumber = :missionNumber")
    Optional<EscortMissions> findByMissionNumber(@Param("missionNumber") String missionNumber);

    @Query("SELECT em FROM EscortMissions em WHERE em.status = :status")
    List<EscortMissions> findByStatus(@Param("status") MissionStatus status);

    @Query("SELECT em FROM EscortMissions em WHERE em.startDate BETWEEN :startDate AND :endDate")
    List<EscortMissions> findByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT em FROM EscortMissions em WHERE em.commercialShip.id = :shipId")
    List<EscortMissions> findByCommercialShipId(@Param("shipId") Long shipId);

    @Query("SELECT em FROM EscortMissions em WHERE em.securityAgency.id = :agencyId")
    List<EscortMissions> findBySecurityAgencyId(@Param("agencyId") Long agencyId);

    @Query("SELECT em FROM EscortMissions em WHERE em.navalVessel.id = :vesselId")
    List<EscortMissions> findByNavalVesselId(@Param("vesselId") Long vesselId);

    @Query("SELECT em FROM EscortMissions em WHERE em.commander.id = :commanderId")
    List<EscortMissions> findByCommanderId(@Param("commanderId") Long commanderId);

    // Methods using trackingId instead of entity ID
    @Query("SELECT em FROM EscortMissions em WHERE em.commercialShip.trackingId = :trackingId")
    List<EscortMissions> findByCommercialShipTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT em FROM EscortMissions em WHERE em.securityAgency.trackingId = :trackingId")
    List<EscortMissions> findBySecurityAgencyTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT em FROM EscortMissions em WHERE em.navalVessel.trackingId = :trackingId")
    List<EscortMissions> findByNavalVesselTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT em FROM EscortMissions em WHERE em.commander.trackingId = :trackingId")
    List<EscortMissions> findByCommanderTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT COUNT(em) FROM EscortMissions em WHERE em.status = :status")
    long countByStatus(@Param("status") MissionStatus status);

    @Query("SELECT COUNT(em) FROM EscortMissions em WHERE em.startDate >= :date")
    long countSince(@Param("date") LocalDateTime date);

    @Query("SELECT em FROM EscortMissions em WHERE em.status = :status AND em.startDate < :date")
    List<EscortMissions> findByStatusAndStartDateBefore(@Param("status") MissionStatus status, @Param("date") LocalDateTime date);

    @Query("SELECT em FROM EscortMissions em WHERE em.status = :status AND em.endDate < :date")
    List<EscortMissions> findByStatusAndEndDateBefore(@Param("status") MissionStatus status, @Param("date") LocalDateTime date);
}
