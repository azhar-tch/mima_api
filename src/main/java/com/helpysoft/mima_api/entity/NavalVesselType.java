package com.helpysoft.mima_api.entity;

/**
 * Types de moyens navals selon le cahier des charges
 */
public enum NavalVesselType {
    PHM("Patrouilleur Hauturier Maritime"),
    VDT_RAPIDE("Vedette Rapide"),
    VDT_COTIERE("Vedette Côtière"),
    VDT("Vedette"),
    EMB_RAPIDE("Embarcation Rapide"),
    EMB_RADE("Embarcation de Rade"),
    SALLE_COM("Salle d'Opérations"); // Pour COM LOME et COM GOUMOU

    private final String description;

    NavalVesselType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
