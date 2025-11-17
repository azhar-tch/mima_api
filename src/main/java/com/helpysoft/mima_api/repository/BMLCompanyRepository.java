package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.BMLCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BMLCompanyRepository extends JpaRepository<BMLCompany, Long> {

    Optional<BMLCompany> findByTrackingId(UUID trackingId);

    Optional<BMLCompany> findByCompanyName(String companyName);

    List<BMLCompany> findByCompanyNameContainingIgnoreCase(String companyName);

    boolean existsByCompanyName(String companyName);
}
