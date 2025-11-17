package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.EscortPersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EscortPersonnelRepository extends JpaRepository<EscortPersonnel, Long> {

    Optional<EscortPersonnel> findByTrackingId(UUID trackingId);

    @Query("SELECT ep FROM EscortPersonnel ep WHERE ep.escortMission.id = :missionId")
    List<EscortPersonnel> findByEscortMissionId(@Param("missionId") Long missionId);

    @Query("SELECT ep FROM EscortPersonnel ep WHERE ep.agent.id = :agentId")
    List<EscortPersonnel> findByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT ep FROM EscortPersonnel ep WHERE ep.agent.id = :agentId AND ep.isFictive = false")
    List<EscortPersonnel> findRealMissionsByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT COUNT(ep) FROM EscortPersonnel ep WHERE ep.agent.id = :agentId AND ep.isFictive = false")
    long countRealMissionsByAgentId(@Param("agentId") Long agentId);

    @Query("SELECT SUM(ep.calculatedAllowance) FROM EscortPersonnel ep WHERE ep.agent.id = :agentId")
    Double sumAllowancesByAgentId(@Param("agentId") Long agentId);
}
