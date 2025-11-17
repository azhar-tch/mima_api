package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.MaritimeRank;
import com.helpysoft.mima_api.entity.PersonnelAllowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonnelAllowanceRepository extends JpaRepository<PersonnelAllowance, Long> {

    Optional<PersonnelAllowance> findByTrackingId(UUID trackingId);

    Optional<PersonnelAllowance> findByRankCode(String rankCode);

    Optional<PersonnelAllowance> findByMaritimeRank(MaritimeRank maritimeRank);

    List<PersonnelAllowance> findByIsActiveTrue();

    @Query("SELECT pa FROM PersonnelAllowance pa WHERE pa.isActive = true ORDER BY pa.escortDailyAllowance DESC")
    List<PersonnelAllowance> findAllOrderByAllowance();
}
