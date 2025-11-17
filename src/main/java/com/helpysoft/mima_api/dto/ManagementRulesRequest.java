package com.helpysoft.mima_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ManagementRulesRequest {
    @NotBlank(message = "Le nom de la règle est requis")
    private String ruleName;

    @NotNull(message = "La règle de prévention de double affectation est requise")
    private Boolean preventDoubleAssignment;

    @NotNull(message = "Le nombre d'heures de repos minimal est requis")
    @Min(value = 0, message = "Le repos minimal doit être positif")
    private Integer minRestHours;

    @NotNull(message = "Le nombre d'heures hebdomadaires maximum est requis")
    @Min(value = 1, message = "Les heures hebdomadaires doivent être positives")
    private Integer maxWeeklyHours;

    @NotNull(message = "La règle de signalement automatique des absences est requise")
    private Boolean autoReportUnjustifiedAbsences;

    @NotNull(message = "La règle d'équité de répartition est requise")
    private Boolean enforceEquityDistribution;

    private String description;

    private LocalDateTime effectiveDate;
}
