package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t FROM Training t WHERE t.trackingId = :trackingId")
    Optional<Training> findByTrackingId(@Param("trackingId") UUID trackingId);

    @Query("SELECT t FROM Training t WHERE t.trainingName = :trainingName")
    Optional<Training> findByTrainingName(@Param("trainingName") String trainingName);

    @Query("SELECT t FROM Training t WHERE t.trainingType = :trainingType")
    List<Training> findByTrainingType(@Param("trainingType") String trainingType);

    @Query("SELECT t FROM Training t WHERE t.country = :country")
    List<Training> findByCountry(@Param("country") String country);

    @Query("SELECT t FROM Training t WHERE t.institution = :institution")
    List<Training> findByInstitution(@Param("institution") String institution);

    @Query("SELECT t FROM Training t WHERE LOWER(t.trainingName) LIKE LOWER(CONCAT('%', :trainingName, '%'))")
    List<Training> findByTrainingNameContainingIgnoreCase(@Param("trainingName") String trainingName);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Training t WHERE t.trainingName = :trainingName")
    boolean existsByTrainingName(@Param("trainingName") String trainingName);
}
