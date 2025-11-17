package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.AgentFunctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentFunctionHistoryRepository extends JpaRepository<AgentFunctionHistory, Long> {

    @Query("SELECT afh FROM AgentFunctionHistory afh WHERE afh.trackingId = :trackingId")
    Optional<AgentFunctionHistory> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT afh FROM AgentFunctionHistory afh WHERE afh.agent.trackingId = :agentTrackingId ORDER BY afh.startDate DESC")
    List<AgentFunctionHistory> findByAgentTrackingIdOrderByStartDateDesc(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT afh FROM AgentFunctionHistory afh WHERE afh.function.trackingId = :functionTrackingId ORDER BY afh.startDate DESC")
    List<AgentFunctionHistory> findByFunctionTrackingId(@Param("functionTrackingId") UUID functionTrackingId);

    @Query("SELECT afh FROM AgentFunctionHistory afh WHERE afh.startDate BETWEEN :startDate AND :endDate ORDER BY afh.startDate DESC")
    List<AgentFunctionHistory> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT afh FROM AgentFunctionHistory afh WHERE afh.agent.trackingId = :agentTrackingId AND afh.endDate IS NULL")
    Optional<AgentFunctionHistory> findCurrentFunctionByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);
}
