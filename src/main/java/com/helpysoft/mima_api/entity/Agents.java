package com.helpysoft.mima_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
public class Agents extends AuditTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true)
    private UUID trackingId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String registrationNo;

    @Column(name = "`rank`", nullable = false)
    private String rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Units unit;

    @Column(nullable = false)
    private Boolean availability = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgentStatus status = AgentStatus.RESTING;

    @Column(nullable = false)
    private char sex;

    private LocalDate dateOfBirth;

    @Column(unique = true)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    private String nationality;
    private String city;
    private String emergencyContact;
    private String maritalStatus;
    private LocalDate recruitmentDate;
    private LocalDate contractEndDate;
    private String idCardNumber;
    private String passportNumber;
    private LocalDate idExpiryDate;
    private String insuranceNumber;
    private String bankAccount;
}
