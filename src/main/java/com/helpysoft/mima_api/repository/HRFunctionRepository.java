package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.HRFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HRFunctionRepository extends JpaRepository<HRFunction, Long> {

    @Query("SELECT f FROM HRFunction f WHERE f.trackingId = :trackingId")
    Optional<HRFunction> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT f FROM HRFunction f WHERE f.functionName = :functionName")
    Optional<HRFunction> findByFunctionName(@Param("functionName") String functionName);

    @Query("SELECT f FROM HRFunction f WHERE f.department = :department")
    List<HRFunction> findByDepartment(@Param("department") String department);

    @Query("SELECT f FROM HRFunction f WHERE LOWER(f.functionName) LIKE LOWER(CONCAT('%', :functionName, '%'))")
    List<HRFunction> findByFunctionNameContainingIgnoreCase(@Param("functionName") String functionName);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM HRFunction f WHERE f.functionName = :functionName")
    boolean existsByFunctionName(@Param("functionName") String functionName);
}
