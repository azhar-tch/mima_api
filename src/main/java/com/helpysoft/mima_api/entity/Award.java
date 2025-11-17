package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name = "awards")
@Data
@EqualsAndHashCode(callSuper = true)
public class Award extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID trackingId;

    @Column(nullable = false, length = 200)
    private String awardName; // Nom de la récompense

    @Column(length = 100)
    private String awardType; // Type de récompense (MEDAILLE, DISTINCTION, etc.)

    @Column(length = 500)
    private String description; // Description de la récompense

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
