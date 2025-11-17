package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.HRGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HRGradeRepository extends JpaRepository<HRGrade, Long> {

    @Query("SELECT g FROM HRGrade g WHERE g.trackingId = :trackingId")
    Optional<HRGrade> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT g FROM HRGrade g WHERE g.gradeName = :gradeName")
    Optional<HRGrade> findByGradeName(@Param("gradeName") String gradeName);

    @Query("SELECT g FROM HRGrade g ORDER BY g.hierarchyLevel ASC")
    List<HRGrade> findAllByOrderByHierarchyLevelAsc();

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM HRGrade g WHERE g.gradeName = :gradeName")
    boolean existsByGradeName(@Param("gradeName") String gradeName);
}
