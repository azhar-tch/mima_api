package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.SecurityAgencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecurityAgencyRepository extends JpaRepository<SecurityAgencies, Long> {

    Optional<SecurityAgencies> findByTrackingId(UUID trackingId);

    Optional<SecurityAgencies> findByAgencyNumber(String agencyNumber);

    List<SecurityAgencies> findByAgencyNameContainingIgnoreCase(String agencyName);

    List<SecurityAgencies> findByIsActiveTrue();

    Optional<SecurityAgencies> findByEmail(String email);

    @Query("SELECT sa FROM SecurityAgencies sa WHERE sa.isActive = true ORDER BY sa.totalEscortsRequested DESC")
    List<SecurityAgencies> findTopAgenciesByEscorts();

    @Query("SELECT sa FROM SecurityAgencies sa WHERE sa.isActive = true ORDER BY sa.totalArmedGuardsRequested DESC")
    List<SecurityAgencies> findTopAgenciesByArmedGuards();

    @Query("SELECT COUNT(sa) FROM SecurityAgencies sa WHERE sa.isActive = true")
    long countActiveAgencies();
}
