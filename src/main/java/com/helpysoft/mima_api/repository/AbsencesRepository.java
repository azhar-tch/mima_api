package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbsencesRepository extends JpaRepository<Absences, Long> {

    @Query("SELECT a FROM Absences a WHERE a.trackingId = :trackingId")
    Optional<Absences> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT COUNT(a) FROM Absences a WHERE a.status = :status")
    Long countByStatus(@Param("status") AbsenceStatus status);

    @Query("SELECT a FROM Absences a WHERE a.agent = :agent")
    List<Absences> findByAgent(@Param("agent") Agents agent);

    @Query("SELECT a FROM Absences a WHERE a.status = :status")
    List<Absences> findByStatus(@Param("status") AbsenceStatus status);

    @Query("SELECT a FROM Absences a WHERE a.absenceType = :absenceType")
    List<Absences> findByAbsenceType(@Param("absenceType") AbsenceType absenceType);

    @Query("SELECT a FROM Absences a WHERE a.agent = :agent AND a.status = :status")
    List<Absences> findByAgentAndStatus(@Param("agent") Agents agent, @Param("status") AbsenceStatus status);

    @Query("SELECT a FROM Absences a WHERE a.startDate BETWEEN :startDate AND :endDate")
    List<Absences> findByStartDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Query("UPDATE Absences a SET a.status = :status, a.validatedBy = :validatedBy WHERE a.trackingId = :trackingId")
    int updateStatusAndValidator(
            @Param("status") AbsenceStatus status,
            @Param("validatedBy") Users validatedBy,
            @Param("trackingId") UUID trackingId
    );
}
