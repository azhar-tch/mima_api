package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.dto.RuleViolation;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.RuleViolationAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuleViolationAlertRepository extends JpaRepository<RuleViolationAlert, Long> {

    @Query("SELECT a FROM RuleViolationAlert a WHERE a.trackingId = :trackingId")
    Optional<RuleViolationAlert> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT a FROM RuleViolationAlert a WHERE a.status = :status ORDER BY a.createDate DESC")
    List<RuleViolationAlert> findByStatus(@Param("status") RuleViolationAlert.AlertStatus status);

    @Query("SELECT a FROM RuleViolationAlert a WHERE a.agent = :agent ORDER BY a.createDate DESC")
    List<RuleViolationAlert> findByAgent(@Param("agent") Agents agent);

    @Query("SELECT a FROM RuleViolationAlert a WHERE a.agent = :agent AND a.status = :status ORDER BY a.createDate DESC")
    List<RuleViolationAlert> findByAgentAndStatus(
            @Param("agent") Agents agent,
            @Param("status") RuleViolationAlert.AlertStatus status
    );

    @Query("SELECT a FROM RuleViolationAlert a WHERE a.ruleType = :ruleType ORDER BY a.createDate DESC")
    List<RuleViolationAlert> findByRuleType(@Param("ruleType") RuleViolation.RuleType ruleType);

    @Query("SELECT a FROM RuleViolationAlert a WHERE a.severity = :severity AND a.status = :status ORDER BY a.createDate DESC")
    List<RuleViolationAlert> findBySeverityAndStatus(
            @Param("severity") RuleViolation.SeverityLevel severity,
            @Param("status") RuleViolationAlert.AlertStatus status
    );

    @Query("SELECT a FROM RuleViolationAlert a WHERE a.createDate BETWEEN :startDate AND :endDate ORDER BY a.createDate DESC")
    List<RuleViolationAlert> findByPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(a) FROM RuleViolationAlert a WHERE a.status = :status")
    Long countByStatus(@Param("status") RuleViolationAlert.AlertStatus status);

    @Query("SELECT COUNT(a) FROM RuleViolationAlert a WHERE a.agent = :agent AND a.status = :status")
    Long countByAgentAndStatus(
            @Param("agent") Agents agent,
            @Param("status") RuleViolationAlert.AlertStatus status
    );
}
