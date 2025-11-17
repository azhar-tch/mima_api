package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Alerts;
import com.helpysoft.mima_api.entity.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertsRepository extends JpaRepository<Alerts, Long> {

    @Query("SELECT a FROM Alerts a WHERE a.trackingId = :trackingId")
    Optional<Alerts> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT a FROM Alerts a WHERE a.agent = :agent")
    List<Alerts> findByAgent(@Param("agent") Agents agent);

    @Query("SELECT a FROM Alerts a WHERE a.status = :status")
    List<Alerts> findByStatus(@Param("status") AlertStatus status);

    @Query("SELECT a FROM Alerts a WHERE a.level = :level")
    List<Alerts> findByLevel(@Param("level") String level);

    @Query("SELECT a FROM Alerts a WHERE a.agent = :agent AND a.status = :status")
    List<Alerts> findByAgentAndStatus(@Param("agent") Agents agent, @Param("status") AlertStatus status);
}
