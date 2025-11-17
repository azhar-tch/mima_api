package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.TrainingRequest;
import com.helpysoft.mima_api.dto.TrainingResponse;

import java.util.List;
import java.util.UUID;

public interface TrainingService {
    TrainingResponse create(TrainingRequest request);
    TrainingResponse update(UUID trackingId, TrainingRequest request);
    TrainingResponse findByTrackingId(UUID trackingId);
    List<TrainingResponse> findByTrainingType(String trainingType);
    List<TrainingResponse> findByCountry(String country);
    List<TrainingResponse> findByInstitution(String institution);
    List<TrainingResponse> searchByName(String trainingName);
    List<TrainingResponse> findAll();
    void delete(UUID trackingId);
}
