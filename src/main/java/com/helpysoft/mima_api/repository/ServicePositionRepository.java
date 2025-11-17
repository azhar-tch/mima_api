package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ServicePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicePositionRepository extends JpaRepository<ServicePosition, Long> {

    @Query("SELECT sp FROM ServicePosition sp WHERE sp.trackingId = :trackingId")
    Optional<ServicePosition> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT sp FROM ServicePosition sp WHERE sp.positionType = :positionType")
    List<ServicePosition> findByPositionType(@Param("positionType") String positionType);

    @Query("SELECT sp FROM ServicePosition sp WHERE sp.location = :location")
    List<ServicePosition> findByLocation(@Param("location") String location);

    @Query("SELECT sp FROM ServicePosition sp WHERE sp.unit = :unit")
    List<ServicePosition> findByUnit(@Param("unit") String unit);

    @Query("SELECT sp FROM ServicePosition sp WHERE LOWER(sp.positionName) LIKE LOWER(CONCAT('%', :positionName, '%'))")
    List<ServicePosition> findByPositionNameContainingIgnoreCase(@Param("positionName") String positionName);
}
