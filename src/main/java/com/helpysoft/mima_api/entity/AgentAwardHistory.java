package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "agent_award_history")
@Data
@EqualsAndHashCode(callSuper = true)
public class AgentAwardHistory extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_tracking_id", referencedColumnName = "trackingId", nullable = false)
    private Agents agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "award_tracking_id", referencedColumnName = "trackingId", nullable = false)
    private Award award;

    @Column(nullable = false)
    private LocalDate awardDate; // Date d'attribution

    @Column(length = 100)
    private String decisionReference; // Référence de la décision

    @Column(length = 500)
    private String motive; // Motif de la récompense

    @Column(length = 500)
    private String remarks; // Observations

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
