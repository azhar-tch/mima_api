package com.helpysoft.mima_api.entity;

/**
 * Types d'escorte
 */
public enum EscortType {
    STANDARD("Escorte standard"),
    HAUTE_SECURITE("Escorte haute sécurité"),
    INTERNATIONALE("Escorte internationale"),
    COTIERE("Escorte côtière"),
    HAUTURIERE("Escorte hauturière"),
    URGENCE("Escorte d'urgence");

    private final String description;

    EscortType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
