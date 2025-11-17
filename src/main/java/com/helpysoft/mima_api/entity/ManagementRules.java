package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class ManagementRules extends AuditTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID trackingId;

    // Règle 1: Aucune double affectation possible sur deux postes simultanés
    @Column(nullable = false)
    private Boolean preventDoubleAssignment = true;

    // Règle 2: Repos minimal obligatoire entre deux gardes ou missions (en heures)
    @Column(nullable = false)
    private Integer minRestHours = 12;

    // Règle 3: Durée maximale hebdomadaire de service à ne pas dépasser (en heures)
    @Column(nullable = false)
    private Integer maxWeeklyHours = 48;

    // Règle 4: Les absences non justifiées sont automatiquement signalées
    @Column(nullable = false)
    private Boolean autoReportUnjustifiedAbsences = true;

    // Règle 5: L'équité de répartition doit être respectée à chaque période
    @Column(nullable = false)
    private Boolean enforceEquityDistribution = true;

    // Métadonnées
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime effectiveDate = LocalDateTime.now();

    // Nom de la règle (pour identification)
    @Column(nullable = false, unique = true)
    private String ruleName;
}
