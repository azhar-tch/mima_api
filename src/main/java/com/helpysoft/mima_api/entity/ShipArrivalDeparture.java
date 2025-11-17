package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité pour la gestion des arrivées et départs des navires de commerce
 * Basé sur le cahier des charges - Table 14 Arrivée et Départ (Page 13)
 * Enregistrement en deux phases: arrivée puis départ du navire
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class ShipArrivalDeparture extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Navire de commerce
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CommercialShips commercialShip;

    /**
     * Date et heure d'arrivée
     */
    @Column(nullable = false)
    private LocalDateTime arrivalDate;

    /**
     * Port de provenance
     */
    @Column(length = 100)
    private String portOfOrigin;

    /**
     * Type de cargaison à l'arrivée
     */
    @Column(length = 200)
    private String cargoTypeArrival;

    /**
     * Quantité de cargaison à l'arrivée (en tonnes)
     */
    @Column
    private Double cargoQuantityArrival;

    /**
     * Nombre de passagers à l'arrivée
     */
    @Column
    private Integer passengersArrival;

    /**
     * Nombre de membres d'équipage
     */
    @Column
    private Integer crewCount;

    /**
     * Nom du capitaine
     */
    @Column(length = 100)
    private String captainName;

    /**
     * Agent maritime
     */
    @Column(length = 200)
    private String shippingAgent;

    /**
     * Poste d'accostage
     */
    @Column(length = 50)
    private String berthingPosition;

    /**
     * Date et heure de départ
     */
    @Column
    private LocalDateTime departureDate;

    /**
     * Port de destination
     */
    @Column(length = 100)
    private String portOfDestination;

    /**
     * Type de cargaison au départ
     */
    @Column(length = 200)
    private String cargoTypeDeparture;

    /**
     * Quantité de cargaison au départ (en tonnes)
     */
    @Column
    private Double cargoQuantityDeparture;

    /**
     * Nombre de passagers au départ
     */
    @Column
    private Integer passengersDeparture;

    /**
     * Durée du séjour au port (en heures)
     */
    @Column
    private Long stayDurationHours;

    /**
     * Services rendus au port (avitaillement, réparation, etc.)
     */
    @Column(length = 500)
    private String servicesProvided;

    /**
     * Montant des droits de port
     */
    @Column
    private Double portDues;

    /**
     * Incidents signalés
     */
    @Column(length = 1000)
    private String incidents;

    /**
     * Observations
     */
    @Column(length = 1000)
    private String observations;

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        calculateStayDuration();
    }

    /**
     * Calcule la durée du séjour au port en heures
     */
    public void calculateStayDuration() {
        if (arrivalDate != null && departureDate != null) {
            this.stayDurationHours = java.time.Duration.between(arrivalDate, departureDate).toHours();
        }
    }

    /**
     * Calcule la durée du séjour en jours
     */
    public Long getStayDurationDays() {
        if (stayDurationHours != null) {
            return stayDurationHours / 24;
        }
        return null;
    }
}
