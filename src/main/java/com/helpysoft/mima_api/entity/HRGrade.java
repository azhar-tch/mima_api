package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name = "hr_grades")
@Data
@EqualsAndHashCode(callSuper = true)
public class HRGrade extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID trackingId;

    @Column(nullable = false, unique = true, length = 100)
    private String gradeName; // Nom du grade (ex: CC, SM, QM1, MLT2, etc.)

    @Column(length = 500)
    private String description; // Description du grade

    @Column(nullable = false)
    private Integer hierarchyLevel; // Niveau hiérarchique pour tri

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
