package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ArmedGuardPersonnels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArmedGuardPersonnelRepository extends JpaRepository<ArmedGuardPersonnels, Long> {

    Optional<ArmedGuardPersonnels> findByTrackingId(UUID trackingId);

    @Query("SELECT agp FROM ArmedGuardPersonnelss agp WHERE agp.armedGuardMission.id = :missionId")
    List<ArmedGuardPersonnels> findByArmedGuardMissionId(@Param("missionId") Long missionId);

    @Query("SELECT agp FROM ArmedGuardPersonnelss agp WHERE agp.agent.id = :agentId")
    List<ArmedGuardPersonnels> findByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT COUNT(agp) FROM ArmedGuardPersonnelss agp WHERE agp.agent.id = :agentId")
    long countMissionsByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT SUM(agp.daysCount) FROM ArmedGuardPersonnelss agp WHERE agp.agent.id = :agentId")
    Integer sumDaysByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT SUM(agp.calculatedAllowance) FROM ArmedGuardPersonnelss agp WHERE agp.agent.id = :agentId")
    Double sumAllowancesByAgentId(@Param("agentId") Long agentId);
}
