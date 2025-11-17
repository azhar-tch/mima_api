package com.helpysoft.mima_api.entity;

/**
 * Spécialités maritimes
 */
public enum MaritimeSpecialty {
    NAVIGATION("Navigation"),
    MECANIQUE("Mécanique"),
    COMMUNICATION("Communication"),
    ARMEMENT("Armement"),
    ELECTRONIQUE("Électronique"),
    SECURITE("Sécurité"),
    PLONGEE("Plongée"),
    AVIATION("Aviation"),
    SANTE("Santé"),
    CUISINE("Cuisine"),
    ADMINISTRATION("Administration"),
    LOGISTIQUE("Logistique"),
    ENERGIE("Énergie"),
    DETECTION("Détection");

    private final String displayName;

    MaritimeSpecialty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
