package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ArmedGuardMission;
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
public interface ArmedGuardMissionRepository extends JpaRepository<ArmedGuardMission, Long> {

    Optional<ArmedGuardMission> findByTrackingId(UUID trackingId);

    Optional<ArmedGuardMission> findByMissionNumber(String missionNumber);

    List<ArmedGuardMission> findByStatus(MissionStatus status);

    @Query("SELECT agm FROM ArmedGuardMission agm WHERE agm.embarkationDate BETWEEN :startDate AND :endDate")
    List<ArmedGuardMission> findByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT agm FROM ArmedGuardMission agm WHERE agm.commercialShip.id = :shipId")
    List<ArmedGuardMission> findByCommercialShipId(@Param("shipId") Long shipId);

    @Query("SELECT agm FROM ArmedGuardMission agm WHERE agm.securityAgency.id = :agencyId")
    List<ArmedGuardMission> findBySecurityAgencyId(@Param("agencyId") Long agencyId);

    @Query("SELECT COUNT(agm) FROM ArmedGuardMission agm WHERE agm.status = :status")
    long countByStatus(@Param("status") MissionStatus status);

    @Query("SELECT SUM(agm.daysCount) FROM ArmedGuardMission agm WHERE agm.commercialShip.id = :shipId")
    Integer sumDaysByShipId(@Param("shipId") Long shipId);
}
