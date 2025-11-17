package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name = "bml_companies")
@Data
@EqualsAndHashCode(callSuper = true)
public class BMLCompany extends AuditTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID trackingId;

    @Column(nullable = false, length = 200)
    private String companyName; // Nom de la compagnie

    @Column(length = 500)
    private String description; // Description

    @PrePersist
    protected void onCreate() {
        if (trackingId == null) {
            trackingId = UUID.randomUUID();
        }
    }
}
