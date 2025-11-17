package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "agent_other_position_history")
@Data
@EqualsAndHashCode(callSuper = true)
public class AgentOtherPositionHistory extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_tracking_id", referencedColumnName = "trackingId", nullable = false)
    private Agent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_tracking_id", referencedColumnName = "trackingId", nullable = false)
    private OtherPosition otherPosition;

    @Column(nullable = false)
    private LocalDate startDate; // Date de début

    @Column
    private LocalDate endDate; // Date de fin

    @Column(length = 200)
    private String location; // Lieu de la position (si applicable)

    @Column(length = 500)
    private String remarks; // Observations

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
