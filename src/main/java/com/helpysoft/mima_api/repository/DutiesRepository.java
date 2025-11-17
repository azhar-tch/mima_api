package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Duties;
import com.helpysoft.mima_api.entity.DutyStatus;
import com.helpysoft.mima_api.entity.Units;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DutiesRepository extends JpaRepository<Duties, Long> {

    @Query("SELECT d FROM Duties d WHERE d.trackingId = :trackingId")
    Optional<Duties> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT COUNT(d) FROM Duties d WHERE d.startDate BETWEEN :startDate AND :endDate")
    Long countByStartDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(d) FROM Duties d WHERE d.status = :status AND d.startDate BETWEEN :startDate AND :endDate")
    Long countByStatusAndStartDateBetween(@Param("status") DutyStatus status, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT d FROM Duties d WHERE d.agent = :agent")
    List<Duties> findByAgent(@Param("agent") Agents agent);

    @Query("SELECT d FROM Duties d WHERE d.unit = :unit")
    List<Duties> findByUnit(@Param("unit") Units unit);

    @Query("SELECT d FROM Duties d WHERE d.status = :status")
    List<Duties> findByStatus(@Param("status") DutyStatus status);

    @Query("SELECT d FROM Duties d WHERE d.agent = :agent AND d.status = :status")
    List<Duties> findByAgentAndStatus(@Param("agent") Agents agent, @Param("status") DutyStatus status);

    @Query("SELECT d FROM Duties d WHERE d.startDate BETWEEN :startDate AND :endDate")
    List<Duties> findByStartDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT d FROM Duties d WHERE d.status = :status AND d.startDate <= :currentDate")
    List<Duties> findByStatusAndStartDateBefore(@Param("status") DutyStatus status, @Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT d FROM Duties d WHERE d.status = :status AND d.endDate <= :currentDate")
    List<Duties> findByStatusAndEndDateBefore(@Param("status") DutyStatus status, @Param("currentDate") LocalDateTime currentDate);
}
