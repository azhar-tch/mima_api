package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.NavalVesselStatus;
import com.helpysoft.mima_api.entity.NavalVesselType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NavalVesselRepository extends JpaRepository<NavalVessels, Long> {

    Optional<NavalVessels> findByTrackingId(UUID trackingId);

    Optional<NavalVessels> findByVesselNumber(String vesselNumber);

    List<NavalVessels> findByVesselType(NavalVesselType vesselType);

    List<NavalVessels> findByOperationalStatus(NavalVesselStatus status);

    List<NavalVessels> findByIsActiveTrue();

    List<NavalVessels> findByVesselNameContainingIgnoreCase(String vesselName);

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.operationalStatus = 'OPERATIONAL' AND nv.isActive = true")
    List<NavalVessels> findAvailableVessels();

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.vesselType IN ('PHM', 'VDT_RAPIDE', 'VDT') AND nv.isActive = true")
    List<NavalVessels> findPatrolVessels();

    @Query("SELECT COUNT(nv) FROM NavalVessels nv WHERE nv.isActive = true")
    long countActiveVessels();
}
