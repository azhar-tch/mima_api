package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.AgentOtherPositionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentOtherPositionHistoryRepository extends JpaRepository<AgentOtherPositionHistory, Long> {

    Optional<AgentOtherPositionHistory> findByTrackingId(UUID trackingId);

    @Query("SELECT aoph FROM AgentOtherPositionHistory aoph WHERE aoph.agent.trackingId = :agentTrackingId ORDER BY aoph.startDate DESC")
    List<AgentOtherPositionHistory> findByAgentTrackingIdOrderByStartDateDesc(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT aoph FROM AgentOtherPositionHistory aoph WHERE aoph.otherPosition.trackingId = :positionTrackingId ORDER BY aoph.startDate DESC")
    List<AgentOtherPositionHistory> findByOtherPositionTrackingId(@Param("positionTrackingId") UUID positionTrackingId);

    @Query("SELECT aoph FROM AgentOtherPositionHistory aoph WHERE aoph.startDate BETWEEN :startDate AND :endDate ORDER BY aoph.startDate DESC")
    List<AgentOtherPositionHistory> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT aoph FROM AgentOtherPositionHistory aoph WHERE aoph.agent.trackingId = :agentTrackingId AND aoph.endDate IS NULL")
    List<AgentOtherPositionHistory> findOngoingPositionsByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);
}
