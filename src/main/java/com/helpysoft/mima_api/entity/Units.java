package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.UUID;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper=false, onlyExplicitlyIncluded = true)
@Entity
public class Units extends AuditTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chief_id")
    private Agents chief;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitStatus status = UnitStatus.INACTIVE;
}
