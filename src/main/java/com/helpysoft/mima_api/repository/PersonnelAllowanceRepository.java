package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.MaritimeRank;
import com.helpysoft.mima_api.entity.PersonnelAllowances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonnelAllowanceRepository extends JpaRepository<PersonnelAllowances, Long> {

    @Query("SELECT pa FROM PersonnelAllowances pa WHERE pa.trackingId = :trackingId")
    Optional<PersonnelAllowances> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT pa FROM PersonnelAllowances pa WHERE pa.rankCode = :rankCode")
    Optional<PersonnelAllowances> findByRankCode(@Param("rankCode") String rankCode);

    @Query("SELECT pa FROM PersonnelAllowances pa WHERE pa.maritimeRank = :maritimeRank")
    Optional<PersonnelAllowances> findByMaritimeRank(@Param("maritimeRank") MaritimeRank maritimeRank);

    @Query("SELECT pa FROM PersonnelAllowances pa WHERE pa.isActive = true")
    List<PersonnelAllowances> findByIsActiveTrue();

    @Query("SELECT pa FROM PersonnelAllowances pa WHERE pa.isActive = true ORDER BY pa.escortDailyAllowance DESC")
    List<PersonnelAllowances> findAllOrderByAllowance();

    @Query("SELECT pa FROM PersonnelAllowances pa WHERE " +
            "LOWER(pa.rankCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pa.currency) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(pa.observations) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PersonnelAllowances> searchPersonnelAllowances(@Param("searchTerm") String searchTerm);
}
