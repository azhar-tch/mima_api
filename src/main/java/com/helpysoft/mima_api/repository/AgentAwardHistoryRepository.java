package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.AgentAwardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentAwardHistoryRepository extends JpaRepository<AgentAwardHistory, Long> {

    @Query("SELECT aah FROM AgentAwardHistory aah WHERE aah.trackingId = :trackingId")
    Optional<AgentAwardHistory> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT aah FROM AgentAwardHistory aah WHERE aah.agent.trackingId = :agentTrackingId ORDER BY aah.awardDate DESC")
    List<AgentAwardHistory> findByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT aah FROM AgentAwardHistory aah WHERE aah.agent.trackingId = :agentTrackingId ORDER BY aah.awardDate DESC")
    List<AgentAwardHistory> findByAgentTrackingIdOrderByAwardDateDesc(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT aah FROM AgentAwardHistory aah WHERE aah.award.trackingId = :awardTrackingId ORDER BY aah.awardDate DESC")
    List<AgentAwardHistory> findByAwardTrackingId(@Param("awardTrackingId") UUID awardTrackingId);

    @Query("SELECT aah FROM AgentAwardHistory aah WHERE aah.awardDate BETWEEN :startDate AND :endDate ORDER BY aah.awardDate DESC")
    List<AgentAwardHistory> findByAwardDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(aah) FROM AgentAwardHistory aah WHERE aah.agent.trackingId = :agentTrackingId")
    Long countByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);
}
