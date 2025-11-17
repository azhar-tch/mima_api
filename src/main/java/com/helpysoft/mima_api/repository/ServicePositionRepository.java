package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ServicePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicePositionRepository extends JpaRepository<ServicePosition, Long> {

    Optional<ServicePosition> findByTrackingId(UUID trackingId);

    List<ServicePosition> findByPositionType(String positionType);

    List<ServicePosition> findByLocation(String location);

    List<ServicePosition> findByUnit(String unit);

    List<ServicePosition> findByPositionNameContainingIgnoreCase(String positionName);
}
