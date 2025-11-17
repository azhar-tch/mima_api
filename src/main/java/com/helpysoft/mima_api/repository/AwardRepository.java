package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    Optional<Award> findByTrackingId(UUID trackingId);

    Optional<Award> findByAwardName(String awardName);

    List<Award> findByAwardType(String awardType);

    List<Award> findByAwardNameContainingIgnoreCase(String awardName);

    boolean existsByAwardName(String awardName);
}
