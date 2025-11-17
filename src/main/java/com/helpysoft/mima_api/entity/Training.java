package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name = "trainings")
@Data
@EqualsAndHashCode(callSuper = true)
public class Training extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID trackingId;

    @Column(nullable = false, length = 200)
    private String trainingName; // Nom du stage/formation

    @Column(length = 100)
    private String trainingType; // Type de stage (SPECIALITE, DIPLOME_MILITAIRE, etc.)

    @Column(length = 500)
    private String description; // Description du stage

    @Column(length = 200)
    private String institution; // Institution de formation

    @Column(length = 100)
    private String country; // Pays de formation

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
