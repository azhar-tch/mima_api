package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Units;
import com.helpysoft.mima_api.entity.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitsRepository extends JpaRepository<Units, Long> {

    @Query("SELECT u FROM Units u WHERE u.trackingId = :trackingId")
    Optional<Units> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT u FROM Units u WHERE u.name = :name")
    Optional<Units> findByName(@Param("name") String name);

    @Query("SELECT u FROM Units u WHERE u.type = :type")
    List<Units> findByType(@Param("type") UnitType type);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Units u WHERE u.name = :name")
    boolean existsByName(@Param("name") String name);
}
