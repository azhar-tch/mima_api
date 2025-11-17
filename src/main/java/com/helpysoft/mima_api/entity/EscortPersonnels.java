package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entité pour la gestion du personnel participant aux missions d'escorte
 * Basé sur le cahier des charges - Table Escorte Personnel
 * Enregistre l'équipage du patrouilleur ayant effectué la mission d'escorte
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "escort_personnel")
public class EscortPersonnels extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    /**
     * Mission d'escorte
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private EscortMissions escortMission;

    /**
     * Membre du personnel
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Agents agent;

    /**
     * Grade au moment de la mission
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaritimeRank rank;

    /**
     * Fonction pendant la mission
     */
    @Column(length = 100)
    private String function;

    /**
     * Personnel fictif (n'ayant pas réellement effectué la mission)
     * Utilisé pour les remplacements administratifs
     */
    @Column
    private Boolean isFictive = false;

    /**
     * Prime calculée pour cette mission (en fonction du grade et de la durée)
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal calculatedAllowance;

    /**
     * Observations
     */
    @Column(length = 500)
    private String observations;

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
        if (isFictive == null) {
            isFictive = false;
        }
    }
}
