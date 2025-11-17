package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name = "service_positions")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServicePosition extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID trackingId;

    @Column(nullable = false, length = 200)
    private String positionName; // Nom de la position de service

    @Column(length = 100)
    private String positionType; // Type (AFFECTATION, DETACHEMENT, etc.)

    @Column(length = 200)
    private String location; // Lieu géographique

    @Column(length = 100)
    private String unit; // Unité/Service

    @Column(length = 500)
    private String description; // Description

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
