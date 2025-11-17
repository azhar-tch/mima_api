package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ArmedGuardMissions;
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
public interface ArmedGuardMissionRepository extends JpaRepository<ArmedGuardMissions, Long> {

    @Query("SELECT agm FROM ArmedGuardMissions agm WHERE agm.trackingId = :trackingId")
    Optional<ArmedGuardMissions> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT agm FROM ArmedGuardMissions agm WHERE agm.missionNumber = :missionNumber")
    Optional<ArmedGuardMissions> findByMissionNumber(@Param("missionNumber") String missionNumber);

    @Query("SELECT agm FROM ArmedGuardMissions agm WHERE agm.status = :status")
    List<ArmedGuardMissions> findByStatus(@Param("status") MissionStatus status);

    @Query("SELECT agm FROM ArmedGuardMissions agm WHERE agm.embarkationDate BETWEEN :startDate AND :endDate")
    List<ArmedGuardMissions> findByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT agm FROM ArmedGuardMissions agm WHERE agm.commercialShip.id = :shipId")
    List<ArmedGuardMissions> findByCommercialShipId(@Param("shipId") Long shipId);

    @Query("SELECT agm FROM ArmedGuardMissions agm WHERE agm.securityAgency.id = :agencyId")
    List<ArmedGuardMissions> findBySecurityAgencyId(@Param("agencyId") Long agencyId);

    @Query("SELECT COUNT(agm) FROM ArmedGuardMissions agm WHERE agm.status = :status")
    long countByStatus(@Param("status") MissionStatus status);

    @Query("SELECT SUM(agm.daysCount) FROM ArmedGuardMissions agm WHERE agm.commercialShip.id = :shipId")
    Integer sumDaysByShipId(@Param("shipId") Long shipId);
}
