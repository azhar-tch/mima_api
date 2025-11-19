package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Histories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HistoriesRepository extends JpaRepository<Histories, Long> {

    @Query("SELECT h FROM Histories h LEFT JOIN FETCH h.agent WHERE h.trackingId = :trackingId")
    Optional<Histories> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT h FROM Histories h LEFT JOIN FETCH h.agent WHERE h.agent = :agent ORDER BY h.createDate DESC")
    List<Histories> findByAgent(@Param("agent") Agents agent);

    @Query("SELECT h FROM Histories h LEFT JOIN FETCH h.agent WHERE h.entityTrackingId = :entityTrackingId ORDER BY h.createDate DESC")
    List<Histories> findByEntityTrackingId(@Param("entityTrackingId") UUID entityTrackingId);

    @Query("SELECT h FROM Histories h LEFT JOIN FETCH h.agent WHERE h.actionType = :actionType ORDER BY h.createDate DESC")
    List<Histories> findByActionType(@Param("actionType") ActionType actionType);

    @Query("SELECT h FROM Histories h LEFT JOIN FETCH h.agent WHERE h.entityName = :entityName ORDER BY h.createDate DESC")
    List<Histories> findByEntityName(@Param("entityName") String entityName);

    @Query("SELECT h FROM Histories h LEFT JOIN FETCH h.agent WHERE h.createDate BETWEEN :startDate AND :endDate ORDER BY h.createDate DESC")
    List<Histories> findByCreateDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT h FROM Histories h LEFT JOIN FETCH h.agent ORDER BY h.createDate DESC")
    List<Histories> findAll();
}
