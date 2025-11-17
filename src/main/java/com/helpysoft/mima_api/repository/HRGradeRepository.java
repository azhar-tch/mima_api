package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.HRGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HRGradeRepository extends JpaRepository<HRGrade, Long> {

    Optional<HRGrade> findByTrackingId(UUID trackingId);

    Optional<HRGrade> findByGradeName(String gradeName);

    List<HRGrade> findAllByOrderByHierarchyLevelAsc();

    boolean existsByGradeName(String gradeName);
}
