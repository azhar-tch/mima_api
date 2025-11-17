package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.SecurityAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecurityAgencyRepository extends JpaRepository<SecurityAgency, Long> {

    Optional<SecurityAgency> findByTrackingId(UUID trackingId);

    Optional<SecurityAgency> findByAgencyNumber(String agencyNumber);

    List<SecurityAgency> findByAgencyNameContainingIgnoreCase(String agencyName);

    List<SecurityAgency> findByIsActiveTrue();

    Optional<SecurityAgency> findByEmail(String email);

    @Query("SELECT sa FROM SecurityAgency sa WHERE sa.isActive = true ORDER BY sa.totalEscortsRequested DESC")
    List<SecurityAgency> findTopAgenciesByEscorts();

    @Query("SELECT sa FROM SecurityAgency sa WHERE sa.isActive = true ORDER BY sa.totalArmedGuardsRequested DESC")
    List<SecurityAgency> findTopAgenciesByArmedGuards();

    @Query("SELECT COUNT(sa) FROM SecurityAgency sa WHERE sa.isActive = true")
    long countActiveAgencies();
}
