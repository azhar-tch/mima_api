package com.helpysoft.mima_api.repository;

import com.helpysoft.mima_api.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    Optional<Training> findByTrackingId(UUID trackingId);

    List<Training> findByTrainingType(String trainingType);

    List<Training> findByCountry(String country);

    List<Training> findByInstitution(String institution);

    List<Training> findByTrainingNameContainingIgnoreCase(String trainingName);
}
