package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false, onlyExplicitlyIncluded = true)
@Entity
public class Missions extends AuditTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String location;

    private String shipName;

    @Column(columnDefinition = "TEXT")
    private String objective;

    @Column(nullable = false)
    private LocalDateTime plannedStartDate;

    @Column(nullable = false)
    private LocalDateTime plannedEndDate;

    private LocalDateTime actualStartDate;

    private LocalDateTime actualEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status = MissionStatus.PLANNED;

    // Relations Many-to-Many pour plusieurs unités
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mission_units",
            joinColumns = @JoinColumn(name = "mission_id"),
            inverseJoinColumns = @JoinColumn(name = "unit_id")
    )
    private Set<Units> units = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mission_agents",
            joinColumns = @JoinColumn(name = "mission_id"),
            inverseJoinColumns = @JoinColumn(name = "agent_id")
    )
    private Set<Agents> agents = new HashSet<>();
}