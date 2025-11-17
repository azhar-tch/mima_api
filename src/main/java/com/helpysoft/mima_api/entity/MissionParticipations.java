package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class MissionParticipations extends AuditTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Missions mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agents agent;

    @Column(nullable = false)
    private String missionRule;

    private Double hoursCompleted = 0.0;
}
