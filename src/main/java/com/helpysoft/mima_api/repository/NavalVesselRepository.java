package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.NavalVessel;
import com.helpysoft.mima_api.entity.NavalVesselStatus;
import com.helpysoft.mima_api.entity.NavalVesselType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NavalVesselRepository extends JpaRepository<NavalVessel, Long> {

    Optional<NavalVessel> findByTrackingId(UUID trackingId);

    Optional<NavalVessel> findByVesselNumber(String vesselNumber);

    List<NavalVessel> findByVesselType(NavalVesselType vesselType);

    List<NavalVessel> findByOperationalStatus(NavalVesselStatus status);

    List<NavalVessel> findByIsActiveTrue();

    List<NavalVessel> findByVesselNameContainingIgnoreCase(String vesselName);

    @Query("SELECT nv FROM NavalVessel nv WHERE nv.operationalStatus = 'OPERATIONAL' AND nv.isActive = true")
    List<NavalVessel> findAvailableVessels();

    @Query("SELECT nv FROM NavalVessel nv WHERE nv.vesselType IN ('PHM', 'VDT_RAPIDE', 'VDT') AND nv.isActive = true")
    List<NavalVessel> findPatrolVessels();

    @Query("SELECT COUNT(nv) FROM NavalVessel nv WHERE nv.isActive = true")
    long countActiveVessels();
}
