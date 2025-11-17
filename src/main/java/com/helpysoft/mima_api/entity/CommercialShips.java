package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour la gestion des navires de commerce
 * Basé sur le cahier des charges - GESTIONS DES NAVIRES DE COMMERCE AU COM
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class CommercialShips extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Numéro IMO - Identifiant unique permanent pour chaque navire
     * Ce numéro reste identique même si le navire change de nom, type ou pavillon
     */
    @Column(nullable = false, unique = true, length = 10)
    private String imoNumber;

    @Column(nullable = false, length = 100)
    private String shipName;

    @Column(length = 50)
    private String shipType; // Pétrolier, Cargo, Porte-conteneurs, etc.

    @Column(length = 50)
    private String flag; // Pavillon

    @Column(length = 9)
    private String mmsi; // Maritime Mobile Service Identity

    @Column(length = 20)
    private String callSign; // Indicatif d'appel

    @Column
    private Integer grossTonnage; // Jauge brute

    @Column
    private Integer deadWeight; // Port en lourd

    @Column
    private Double length; // Longueur en mètres

    @Column
    private Double width; // Largeur en mètres

    @Column
    private Double draft; // Tirant d'eau en mètres

    @Column
    private Integer yearBuilt; // Année de construction

    @Column(length = 200)
    private String shipOwner; // Armateur

    @Column(length = 200)
    private String operator; // Exploitant

    @Column(length = 100)
    private String lastPort; // Dernier port de provenance

    @Column(length = 100)
    private String nextPort; // Prochain port de destination

    @Column(length = 100)
    private String cargoType; // Type de cargaison

    @Column
    private LocalDateTime arrivalDate; // Date d'arrivée à Lomé

    @Column
    private LocalDateTime departureDate; // Date de départ de Lomé

    @Column(length = 50)
    private String status; // En mer, Au port, En escorte, etc.

    @Column(length = 1000)
    private String observations; // Observations diverses

    @Column
    private Boolean isActive = true; // Navire actif ou réformé

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
