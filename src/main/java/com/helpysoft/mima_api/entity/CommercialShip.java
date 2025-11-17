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
@Table(name = "commercial_ships")
public class CommercialShip extends AuditTable implements Serializable {

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
    @Column(name = "imo_number", nullable = false, unique = true, length = 10)
    private String imoNumber;

    @Column(name = "ship_name", nullable = false, length = 100)
    private String shipName;

    @Column(name = "ship_type", length = 50)
    private String shipType; // Pétrolier, Cargo, Porte-conteneurs, etc.

    @Column(name = "flag", length = 50)
    private String flag; // Pavillon

    @Column(name = "mmsi", length = 9)
    private String mmsi; // Maritime Mobile Service Identity

    @Column(name = "call_sign", length = 20)
    private String callSign; // Indicatif d'appel

    @Column(name = "gross_tonnage")
    private Integer grossTonnage; // Jauge brute

    @Column(name = "dead_weight")
    private Integer deadWeight; // Port en lourd

    @Column(name = "length")
    private Double length; // Longueur en mètres

    @Column(name = "width")
    private Double width; // Largeur en mètres

    @Column(name = "draft")
    private Double draft; // Tirant d'eau en mètres

    @Column(name = "year_built")
    private Integer yearBuilt; // Année de construction

    @Column(name = "ship_owner", length = 200)
    private String shipOwner; // Armateur

    @Column(name = "operator", length = 200)
    private String operator; // Exploitant

    @Column(name = "last_port", length = 100)
    private String lastPort; // Dernier port de provenance

    @Column(name = "next_port", length = 100)
    private String nextPort; // Prochain port de destination

    @Column(name = "cargo_type", length = 100)
    private String cargoType; // Type de cargaison

    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate; // Date d'arrivée à Lomé

    @Column(name = "departure_date")
    private LocalDateTime departureDate; // Date de départ de Lomé

    @Column(name = "status", length = 50)
    private String status; // En mer, Au port, En escorte, etc.

    @Column(name = "observations", length = 1000)
    private String observations; // Observations diverses

    @Column(name = "is_active")
    private Boolean isActive = true; // Navire actif ou réformé

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
