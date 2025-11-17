package com.helpysoft.mima_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagementRulesResponse {
    private UUID trackingId;
    private String ruleName;

    // Règle 1: Aucune double affectation possible sur deux postes simultanés
    private Boolean preventDoubleAssignment;

    // Règle 2: Repos minimal obligatoire entre deux gardes ou missions (en heures)
    private Integer minRestHours;

    // Règle 3: Durée maximale hebdomadaire de service à ne pas dépasser (en heures)
    private Integer maxWeeklyHours;

    // Règle 4: Les absences non justifiées sont automatiquement signalées
    private Boolean autoReportUnjustifiedAbsences;

    // Règle 5: L'équité de répartition doit être respectée à chaque période
    private Boolean enforceEquityDistribution;

    // Métadonnées
    private String description;
    private LocalDateTime effectiveDate;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
