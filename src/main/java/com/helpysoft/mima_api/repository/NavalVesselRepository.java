package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.NavalVesselStatus;
import com.helpysoft.mima_api.entity.NavalVesselType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NavalVesselRepository extends JpaRepository<NavalVessels, Long> {

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.trackingId = :trackingId")
    Optional<NavalVessels> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.vesselNumber = :vesselNumber")
    Optional<NavalVessels> findByVesselNumber(@Param("vesselNumber") String vesselNumber);

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.vesselType = :vesselType")
    List<NavalVessels> findByVesselType(@Param("vesselType") NavalVesselType vesselType);

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.operationalStatus = :status")
    List<NavalVessels> findByOperationalStatus(@Param("status") NavalVesselStatus status);

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.isActive = true")
    List<NavalVessels> findByIsActiveTrue();

    @Query("SELECT nv FROM NavalVessels nv WHERE LOWER(nv.vesselName) LIKE LOWER(CONCAT('%', :vesselName, '%'))")
    List<NavalVessels> findByVesselNameContainingIgnoreCase(@Param("vesselName") String vesselName);

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.operationalStatus = 'OPERATIONAL' AND nv.isActive = true")
    List<NavalVessels> findAvailableVessels();

    @Query("SELECT nv FROM NavalVessels nv WHERE nv.vesselType IN ('PHM', 'VDT_RAPIDE', 'VDT') AND nv.isActive = true")
    List<NavalVessels> findPatrolVessels();

    @Query("SELECT COUNT(nv) FROM NavalVessels nv WHERE nv.isActive = true")
    long countActiveVessels();
}
