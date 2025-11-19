package com.helpysoft.mima_api.entity;

/**
 * Statut des navires commerciaux
 */
public enum ShipStatus {
    IN_PORT("Au port"),
    AT_SEA("En mer"),
    UNDER_ESCORT("En escorte"),
    WAITING("En attente"),
    LOADING("En chargement"),
    UNLOADING("En déchargement"),
    IN_MAINTENANCE("En maintenance"),
    IN_REPAIR("En réparation"),
    OTHER("Autre");

    private final String description;

    ShipStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
