package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ShipProvisioning;
import com.helpysoft.mima_api.entity.CommercialShips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipProvisioningRepository extends JpaRepository<ShipProvisioning, Long> {

    Optional<ShipProvisioning> findByTrackingId(UUID trackingId);

    List<ShipProvisioning> findByCommercialShip(CommercialShips commercialShip);

    List<ShipProvisioning> findByCommercialShipOrderByProvisioningDateDesc(CommercialShips commercialShip);

    List<ShipProvisioning> findByProvisioningType(String provisioningType);

    @Query("SELECT s FROM ShipProvisioning s WHERE s.provisioningDate BETWEEN :startDate AND :endDate")
    List<ShipProvisioning> findByProvisioningDateBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    // Avitaillements avec retard
    @Query("SELECT s FROM ShipProvisioning s WHERE s.hasDelay = true ORDER BY s.provisioningDate DESC")
    List<ShipProvisioning> findProvisioningsWithDelay();

    // Par fournisseur
    @Query("SELECT s.supplierName, COUNT(s) FROM ShipProvisioning s GROUP BY s.supplierName")
    List<Object[]> countBySupplier();

    // Par type d'avitaillement
    @Query("SELECT s.provisioningType, COUNT(s), SUM(s.amount) FROM ShipProvisioning s GROUP BY s.provisioningType")
    List<Object[]> statisticsByProvisioningType();

    // Avitaillements par bateau de servitude
    List<ShipProvisioning> findBySupplyVesselName(String supplyVesselName);

    // Avitaillements par ID de navire commercial
    @Query("SELECT s FROM ShipProvisioning s WHERE s.commercialShip.id = :shipId")
    List<ShipProvisioning> findByCommercialShipId(@Param("shipId") Long shipId);

    // Avitaillements par fournisseur
    List<ShipProvisioning> findBySupplierName(String supplierName);

    // Avitaillements par type de produit
    List<ShipProvisioning> findByProductType(String productType);
}
