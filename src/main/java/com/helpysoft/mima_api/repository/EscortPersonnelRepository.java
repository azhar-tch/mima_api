package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.EscortPersonnels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EscortPersonnelRepository extends JpaRepository<EscortPersonnels, Long> {

    @Query("SELECT ep FROM EscortPersonnels ep WHERE ep.trackingId = :trackingId")
    Optional<EscortPersonnels> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT ep FROM EscortPersonnels ep WHERE ep.escortMission.id = :missionId")
    List<EscortPersonnels> findByEscortMissionId(@Param("missionId") Long missionId);

    @Query("SELECT ep FROM EscortPersonnels ep WHERE ep.agent.id = :agentId")
    List<EscortPersonnels> findByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT ep FROM EscortPersonnels ep WHERE ep.agent.id = :agentId AND ep.isFictive = false")
    List<EscortPersonnels> findRealMissionsByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT COUNT(ep) FROM EscortPersonnels ep WHERE ep.agent.id = :agentId AND ep.isFictive = false")
    long countRealMissionsByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT SUM(ep.calculatedAllowance) FROM EscortPersonnels ep WHERE ep.agent.id = :agentId")
    Double sumAllowancesByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT ep FROM EscortPersonnels ep " +
            "WHERE ep.agent = :agent " +
            "AND ep.escortMission.status = :status " +
            "AND ep.isFictive = false")
    List<EscortPersonnels> findByAgentAndMissionStatus(
            @Param("agent") com.helpysoft.mima_api.entity.Agents agent,
            @Param("status") com.helpysoft.mima_api.entity.MissionStatus status);
}
