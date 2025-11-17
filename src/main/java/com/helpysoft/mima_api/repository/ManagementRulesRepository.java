package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.ManagementRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManagementRulesRepository extends JpaRepository<ManagementRules, Long> {

    @Query("SELECT mr FROM ManagementRules mr WHERE mr.trackingId = :trackingId")
    Optional<ManagementRules> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT mr FROM ManagementRules mr WHERE mr.ruleName = :ruleName")
    Optional<ManagementRules> findByRuleName(@Param("ruleName") String ruleName);
}
