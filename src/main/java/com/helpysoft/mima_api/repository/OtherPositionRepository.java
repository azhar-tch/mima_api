package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.OtherPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtherPositionRepository extends JpaRepository<OtherPosition, Long> {

    Optional<OtherPosition> findByTrackingId(UUID trackingId);

    List<OtherPosition> findByPositionType(String positionType);

    List<OtherPosition> findByPositionNameContainingIgnoreCase(String positionName);

    Optional<OtherPosition> findByPositionName(String positionName);
}
