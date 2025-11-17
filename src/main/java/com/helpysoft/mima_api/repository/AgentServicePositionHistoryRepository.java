package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.AgentServicePositionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentServicePositionHistoryRepository extends JpaRepository<AgentServicePositionHistory, Long> {

    @Query("SELECT asph FROM AgentServicePositionHistory asph WHERE asph.trackingId = :trackingId")
    Optional<AgentServicePositionHistory> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT asph FROM AgentServicePositionHistory asph WHERE asph.agent.trackingId = :agentTrackingId ORDER BY asph.startDate DESC")
    List<AgentServicePositionHistory> findByAgentTrackingIdOrderByStartDateDesc(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT asph FROM AgentServicePositionHistory asph WHERE asph.servicePosition.trackingId = :positionTrackingId ORDER BY asph.startDate DESC")
    List<AgentServicePositionHistory> findByServicePositionTrackingId(@Param("positionTrackingId") UUID positionTrackingId);

    @Query("SELECT asph FROM AgentServicePositionHistory asph WHERE asph.startDate BETWEEN :startDate AND :endDate ORDER BY asph.startDate DESC")
    List<AgentServicePositionHistory> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT asph FROM AgentServicePositionHistory asph WHERE asph.agent.trackingId = :agentTrackingId AND asph.endDate IS NULL")
    Optional<AgentServicePositionHistory> findCurrentPositionByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);
}
