package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.HRFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HRFunctionRepository extends JpaRepository<HRFunction, Long> {

    Optional<HRFunction> findByTrackingId(UUID trackingId);

    Optional<HRFunction> findByFunctionName(String functionName);

    List<HRFunction> findByDepartment(String department);

    List<HRFunction> findByFunctionNameContainingIgnoreCase(String functionName);

    boolean existsByFunctionName(String functionName);
}
