package com.helpysoft.mima_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Absences extends AuditTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID trackingId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbsenceType absenceType;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    private String justification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbsenceStatus status = AbsenceStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agents agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validated_by_id")
    private Users validatedBy;
}
