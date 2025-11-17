package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.AgentTrainingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentTrainingHistoryRepository extends JpaRepository<AgentTrainingHistory, Long> {

    @Query("SELECT ath FROM AgentTrainingHistory ath WHERE ath.trackingId = :trackingId")
    Optional<AgentTrainingHistory> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT ath FROM AgentTrainingHistory ath WHERE ath.agent.trackingId = :agentTrackingId ORDER BY ath.startDate DESC")
    List<AgentTrainingHistory> findByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT ath FROM AgentTrainingHistory ath WHERE ath.agent.trackingId = :agentTrackingId ORDER BY ath.startDate DESC")
    List<AgentTrainingHistory> findByAgentTrackingIdOrderByStartDateDesc(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT ath FROM AgentTrainingHistory ath WHERE ath.training.trackingId = :trainingTrackingId ORDER BY ath.startDate DESC")
    List<AgentTrainingHistory> findByTrainingTrackingId(@Param("trainingTrackingId") UUID trainingTrackingId);

    @Query("SELECT ath FROM AgentTrainingHistory ath WHERE ath.startDate BETWEEN :startDate AND :endDate ORDER BY ath.startDate DESC")
    List<AgentTrainingHistory> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT ath FROM AgentTrainingHistory ath WHERE ath.agent.trackingId = :agentTrackingId AND ath.endDate IS NULL")
    List<AgentTrainingHistory> findOngoingTrainingsByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);
}
