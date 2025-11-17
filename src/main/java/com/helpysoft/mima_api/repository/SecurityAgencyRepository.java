package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.SecurityAgencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecurityAgencyRepository extends JpaRepository<SecurityAgencies, Long> {

    @Query("SELECT sa FROM SecurityAgencies sa WHERE sa.trackingId = :trackingId")
    Optional<SecurityAgencies> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT sa FROM SecurityAgencies sa WHERE sa.agencyNumber = :agencyNumber")
    Optional<SecurityAgencies> findByAgencyNumber(@Param("agencyNumber") String agencyNumber);

    @Query("SELECT sa FROM SecurityAgencies sa WHERE LOWER(sa.agencyName) LIKE LOWER(CONCAT('%', :agencyName, '%'))")
    List<SecurityAgencies> findByAgencyNameContainingIgnoreCase(@Param("agencyName") String agencyName);

    @Query("SELECT sa FROM SecurityAgencies sa WHERE sa.isActive = true")
    List<SecurityAgencies> findByIsActiveTrue();

    @Query("SELECT sa FROM SecurityAgencies sa WHERE sa.email = :email")
    Optional<SecurityAgencies> findByEmail(@Param("email") String email);

    @Query("SELECT sa FROM SecurityAgencies sa WHERE sa.isActive = true ORDER BY sa.totalEscortsRequested DESC")
    List<SecurityAgencies> findTopAgenciesByEscorts();

    @Query("SELECT sa FROM SecurityAgencies sa WHERE sa.isActive = true ORDER BY sa.totalArmedGuardsRequested DESC")
    List<SecurityAgencies> findTopAgenciesByArmedGuards();

    @Query("SELECT COUNT(sa) FROM SecurityAgencies sa WHERE sa.isActive = true")
    long countActiveAgencies();
}
