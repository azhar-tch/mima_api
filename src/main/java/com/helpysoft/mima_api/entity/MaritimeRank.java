package com.helpysoft.mima_api.entity;

/**
 * Grades maritimes pour les marins
 */
public enum MaritimeRank {
    // Officiers
    CAPITAINE("Capitaine"),
    COMMANDANT("Commandant"),
    LIEUTENANT("Lieutenant"),
    ENSEIGNE("Enseigne"),

    // Officiers Mariniers
    MAITRE_PRINCIPAL("Maître Principal"),
    PREMIER_MAITRE("Premier Maître"),
    MAITRE("Maître"),
    SECOND_MAITRE("Second Maître"),

    // Quartier-Maîtres et Matelots
    QUARTIER_MAITRE_1ERE_CLASSE("Quartier-Maître 1ère Classe"),
    QUARTIER_MAITRE_2EME_CLASSE("Quartier-Maître 2ème Classe"),
    MATELOT_BREVETÉ("Matelot Breveté"),
    MATELOT("Matelot");

    private final String displayName;

    MaritimeRank(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
