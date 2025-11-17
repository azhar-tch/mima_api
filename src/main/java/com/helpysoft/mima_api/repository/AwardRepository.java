package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    @Query("SELECT a FROM Award a WHERE a.trackingId = :trackingId")
    Optional<Award> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT a FROM Award a WHERE a.awardName = :awardName")
    Optional<Award> findByAwardName(@Param("awardName") String awardName);

    @Query("SELECT a FROM Award a WHERE a.awardType = :awardType")
    List<Award> findByAwardType(@Param("awardType") String awardType);

    @Query("SELECT a FROM Award a WHERE LOWER(a.awardName) LIKE LOWER(CONCAT('%', :awardName, '%'))")
    List<Award> findByAwardNameContainingIgnoreCase(@Param("awardName") String awardName);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Award a WHERE a.awardName = :awardName")
    boolean existsByAwardName(@Param("awardName") String awardName);
}
