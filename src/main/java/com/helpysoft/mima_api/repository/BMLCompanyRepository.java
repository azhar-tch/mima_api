package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.BMLCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BMLCompanyRepository extends JpaRepository<BMLCompany, Long> {

    @Query("SELECT b FROM BMLCompany b WHERE b.trackingId = :trackingId")
    Optional<BMLCompany> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT b FROM BMLCompany b WHERE b.companyName = :companyName")
    Optional<BMLCompany> findByCompanyName(@Param("companyName") String companyName);

    @Query("SELECT b FROM BMLCompany b WHERE LOWER(b.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))")
    List<BMLCompany> findByCompanyNameContainingIgnoreCase(@Param("companyName") String companyName);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BMLCompany b WHERE b.companyName = :companyName")
    boolean existsByCompanyName(@Param("companyName") String companyName);
}
