package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.AgentGradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentGradeHistoryRepository extends JpaRepository<AgentGradeHistory, Long> {

    Optional<AgentGradeHistory> findByTrackingId(UUID trackingId);

    @Query("SELECT agh FROM AgentGradeHistory agh WHERE agh.agent.trackingId = :agentTrackingId ORDER BY agh.promotionDate DESC")
    List<AgentGradeHistory> findByAgentTrackingIdOrderByPromotionDateDesc(@Param("agentTrackingId") UUID agentTrackingId);

    @Query("SELECT agh FROM AgentGradeHistory agh WHERE agh.grade.trackingId = :gradeTrackingId ORDER BY agh.promotionDate DESC")
    List<AgentGradeHistory> findByGradeTrackingId(@Param("gradeTrackingId") UUID gradeTrackingId);

    @Query("SELECT agh FROM AgentGradeHistory agh WHERE agh.promotionDate BETWEEN :startDate AND :endDate ORDER BY agh.promotionDate DESC")
    List<AgentGradeHistory> findByPromotionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT agh FROM AgentGradeHistory agh WHERE agh.agent.trackingId = :agentTrackingId ORDER BY agh.promotionDate DESC")
    Optional<AgentGradeHistory> findLatestGradeByAgentTrackingId(@Param("agentTrackingId") UUID agentTrackingId);
}
