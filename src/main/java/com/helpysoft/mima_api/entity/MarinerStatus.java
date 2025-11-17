package com.helpysoft.mima_api.entity;

/**
 * Statut du marin
 */
public enum MarinerStatus {
    DISPONIBLE("Disponible"), // Disponible pour affectation
    EN_MER("En mer"), // En mission maritime
    EN_GARDE("En garde"), // De garde
    PERMISSION("Permission"), // En permission/repos
    ABSENT("Absent"), // Absent (maladie, congé, etc.)
    EN_FORMATION("En formation"), // En formation
    INDISPONIBLE("Indisponible"); // Temporairement indisponible

    private final String displayName;

    MarinerStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
