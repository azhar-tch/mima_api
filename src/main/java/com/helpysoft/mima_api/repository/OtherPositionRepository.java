package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.OtherPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtherPositionRepository extends JpaRepository<OtherPosition, Long> {

    @Query("SELECT op FROM OtherPosition op WHERE op.trackingId = :trackingId")
    Optional<OtherPosition> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT op FROM OtherPosition op WHERE op.positionType = :positionType")
    List<OtherPosition> findByPositionType(@Param("positionType") String positionType);

    @Query("SELECT op FROM OtherPosition op WHERE LOWER(op.positionName) LIKE LOWER(CONCAT('%', :positionName, '%'))")
    List<OtherPosition> findByPositionNameContainingIgnoreCase(@Param("positionName") String positionName);

    @Query("SELECT op FROM OtherPosition op WHERE op.positionName = :positionName")
    Optional<OtherPosition> findByPositionName(@Param("positionName") String positionName);

    @Query("SELECT CASE WHEN COUNT(op) > 0 THEN true ELSE false END FROM OtherPosition op WHERE op.positionName = :positionName")
    boolean existsByPositionName(@Param("positionName") String positionName);
}
