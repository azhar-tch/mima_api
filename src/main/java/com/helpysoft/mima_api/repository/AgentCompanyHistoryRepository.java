package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.AgentCompanyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentCompanyHistoryRepository extends JpaRepository<AgentCompanyHistory, Long> {

    @Query("SELECT ach FROM AgentCompanyHistory ach WHERE ach.trackingId = :trackingId")
    Optional<AgentCompanyHistory> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT ach FROM AgentCompanyHistory ach WHERE ach.agent.trackingId = :agentTrackingId ORDER BY ach.startDate DESC")
    List<AgentCompanyHistory> findByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT ach FROM AgentCompanyHistory ach WHERE ach.agent.trackingId = :agentTrackingId ORDER BY ach.startDate DESC")
    List<AgentCompanyHistory> findByAgentTrackingIdOrderByStartDateDesc(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT ach FROM AgentCompanyHistory ach WHERE ach.company.trackingId = :companyTrackingId ORDER BY ach.startDate DESC")
    List<AgentCompanyHistory> findByCompanyTrackingId(@Param("companyTrackingId") UUID companyTrackingId);

    @Query("SELECT ach FROM AgentCompanyHistory ach WHERE ach.startDate BETWEEN :startDate AND :endDate ORDER BY ach.startDate DESC")
    List<AgentCompanyHistory> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT ach FROM AgentCompanyHistory ach WHERE ach.agent.trackingId = :agentTrackingId AND ach.endDate IS NULL")
    Optional<AgentCompanyHistory> findCurrentCompanyByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT ach FROM AgentCompanyHistory ach WHERE ach.company.trackingId = :companyTrackingId AND ach.endDate IS NULL")
    List<AgentCompanyHistory> findCurrentMembersByCompanyTrackingId(@Param("companyTrackingId") UUID companyTrackingId);
}
