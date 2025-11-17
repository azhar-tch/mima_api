package com.helpysoft.mima_api.entity;

/**
 * Types de navires sur lesquels le marin est qualifié
 */
public enum VesselType {
    FREGATE("Frégate"),
    CORVETTE("Corvette"),
    PATROUILLEUR("Patrouilleur"),
    SOUS_MARIN("Sous-marin"),
    PORTE_AVIONS("Porte-avions"),
    BATIMENT_AMPHIBIE("Bâtiment amphibie"),
    RAVITAILLEUR("Ravitailleur"),
    DRAGUEUR("Dragueur de mines"),
    REMORQUEUR("Remorqueur"),
    VEDETTE("Vedette"),
    NAVIRE_ECOLE("Navire école");

    private final String displayName;

    VesselType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
