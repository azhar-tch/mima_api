package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "agent_training_history")
@Data
@EqualsAndHashCode(callSuper = true)
public class AgentTrainingHistory extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_tracking_id", referencedColumnName = "trackingId", nullable = false)
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_tracking_id", referencedColumnName = "trackingId", nullable = false)
    private Training training;

    @Column(nullable = false)
    private LocalDate startDate; // Date de début du stage

    @Column
    private LocalDate endDate; // Date de fin du stage

    @Column(length = 100)
    private String diploma; // Diplôme obtenu

    @Column(length = 500)
    private String remarks; // Observations

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
