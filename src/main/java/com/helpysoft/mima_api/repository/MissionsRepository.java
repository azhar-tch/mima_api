package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Missions;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.entity.Units;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MissionsRepository extends JpaRepository<Missions, Long> {

    @Query("SELECT m FROM Missions m WHERE m.trackingId = :trackingId")
    Optional<Missions> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT COUNT(m) FROM Missions m WHERE m.status = :status")
    Long countByStatus(@Param("status") MissionStatus status);

    @Query("SELECT COUNT(m) FROM Missions m WHERE m.createDate BETWEEN :startDate AND :endDate")
    Long countByCreateDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM Missions m WHERE m.status = :status")
    List<Missions> findByStatus(@Param("status") MissionStatus status);

    @Query("SELECT m FROM Missions m WHERE :unit MEMBER OF m.units")
    List<Missions> findByUnit(@Param("unit") Units unit);

    @Query("SELECT m FROM Missions m WHERE m.plannedStartDate BETWEEN :startDate AND :endDate")
    List<Missions> findByPlannedStartDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM Missions m WHERE m.status = :status AND m.plannedStartDate < :currentDate")
    List<Missions> findByStatusAndPlannedStartDateBefore(@Param("status") MissionStatus status, @Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT m FROM Missions m WHERE m.status = :status AND m.plannedEndDate < :currentDate")
    List<Missions> findByStatusAndPlannedEndDateBefore(@Param("status") MissionStatus status, @Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT m FROM Missions m WHERE :agent MEMBER OF m.agents AND m.status = :status")
    List<Missions> findByAgentAndStatus(@Param("agent") com.helpysoft.mima_api.entity.Agents agent, @Param("status") MissionStatus status);
}
