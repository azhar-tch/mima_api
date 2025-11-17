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
public class EscortPersonnel extends AuditTable implements Serializable {

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
    @JoinColumn(name = "escort_mission_id", nullable = false)
    private EscortMission escortMission;

    /**
     * Membre du personnel
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agents agent;

    /**
     * Grade au moment de la mission
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rank", nullable = false)
    private MaritimeRank rank;

    /**
     * Fonction pendant la mission
     */
    @Column(name = "function", length = 100)
    private String function;

    /**
     * Personnel fictif (n'ayant pas réellement effectué la mission)
     * Utilisé pour les remplacements administratifs
     */
    @Column(name = "is_fictive")
    private Boolean isFictive = false;

    /**
     * Prime calculée pour cette mission (en fonction du grade et de la durée)
     */
    @Column(name = "calculated_allowance", precision = 10, scale = 2)
    private BigDecimal calculatedAllowance;

    /**
     * Observations
     */
    @Column(name = "observations", length = 500)
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
