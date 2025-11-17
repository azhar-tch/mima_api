package com.helpysoft.mima_api.entity;

/**
 * Statut opérationnel des moyens navals
 */
public enum NavalVesselStatus {
    OPERATIONAL("Opérationnel"),
    IN_MAINTENANCE("En maintenance"),
    IN_REPAIR("En réparation"),
    ON_MISSION("En mission"),
    IN_PORT("Au port"),
    REFITTING("En refit"),
    DECOMMISSIONED("Réformé"),
    UNAVAILABLE("Indisponible");

    private final String description;

    NavalVesselStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
