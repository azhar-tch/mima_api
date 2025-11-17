package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.MaritimeRank;
import com.helpysoft.mima_api.entity.PersonnelAllowances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonnelAllowanceRepository extends JpaRepository<PersonnelAllowances, Long> {

    Optional<PersonnelAllowances> findByTrackingId(UUID trackingId);

    Optional<PersonnelAllowances> findByRankCode(String rankCode);

    Optional<PersonnelAllowances> findByMaritimeRank(MaritimeRank maritimeRank);

    List<PersonnelAllowances> findByIsActiveTrue();

    @Query("SELECT pa FROM PersonnelAllowances pa WHERE pa.isActive = true ORDER BY pa.escortDailyAllowance DESC")
    List<PersonnelAllowances> findAllOrderByAllowance();
}
