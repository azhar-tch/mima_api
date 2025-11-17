package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.MissionParticipations;
import com.helpysoft.mima_api.entity.Missions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MissionParticipationsRepository extends JpaRepository<MissionParticipations, Long> {

    @Query("SELECT mp FROM MissionParticipations mp WHERE mp.trackingId = :trackingId")
    Optional<MissionParticipations> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT mp FROM MissionParticipations mp WHERE mp.mission = :mission")
    List<MissionParticipations> findByMission(@Param("mission") Missions mission);

    @Query("SELECT mp FROM MissionParticipations mp WHERE mp.agent = :agent")
    List<MissionParticipations> findByAgent(@Param("agent") Agents agent);

    @Query("SELECT mp FROM MissionParticipations mp WHERE mp.mission = :mission AND mp.agent = :agent")
    List<MissionParticipations> findByMissionAndAgent(@Param("mission") Missions mission, @Param("agent") Agents agent);
}
