package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RulesRepository extends JpaRepository<Rules, Long> {

    @Query("SELECT r FROM Rules r WHERE r.trackingId = :trackingId")
    Optional<Rules> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT r FROM Rules r WHERE r.title = :title")
    Optional<Rules> findByTitle(@Param("title") String title);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rules r WHERE r.title = :title")
    boolean existsByTitle(@Param("title") String title);
}
