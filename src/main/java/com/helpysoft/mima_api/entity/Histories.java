package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Histories extends AuditTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID trackingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agents agent;

    @Column(nullable = false)
    private String entityName; // "MISSION", "DUTY", "ABSENCE", "REST"

    @Column(nullable = false)
    private UUID entityTrackingId; // UUID de l'entité concernée

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    @Column(columnDefinition = "TEXT")
    private String changesSummary; // Message lisible: "Position: 'Capitaine' → 'Second' | ..."

    @Column(columnDefinition = "TEXT")
    private String oldValue; // État complet avant modification (JSON)

    @Column(columnDefinition = "TEXT")
    private String newValue; // État complet après modification (JSON)

    @Column(columnDefinition = "TEXT")
    private String details; // Détails supplémentaires si nécessaire
}
