package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.TrainingRequest;
import com.helpysoft.mima_api.dto.TrainingResponse;
import com.helpysoft.mima_api.entity.Training;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TrainingMapper {

    public Training toEntity(TrainingRequest request) {
        Training training = new Training();
        training.setTrackingId(UUID.randomUUID());
        training.setTrainingName(request.getTrainingName());
        training.setTrainingType(request.getTrainingType());
        training.setDescription(request.getDescription());
        training.setInstitution(request.getInstitution());
        training.setCountry(request.getCountry());
        return training;
    }

    public TrainingResponse toResponse(Training training) {
        TrainingResponse response = new TrainingResponse();
        response.setTrackingId(training.getTrackingId());
        response.setTrainingName(training.getTrainingName());
        response.setTrainingType(training.getTrainingType());
        response.setDescription(training.getDescription());
        response.setInstitution(training.getInstitution());
        response.setCountry(training.getCountry());
        response.setCreateDate(training.getCreateDate());
        response.setUpdateDate(training.getUpdateDate());
        response.setCreatedBy(training.getCreatedBy());
        response.setUpdatedBy(training.getUpdatedBy());
        return response;
    }

    public void updateEntity(Training training, TrainingRequest request) {
        training.setTrainingName(request.getTrainingName());
        training.setTrainingType(request.getTrainingType());
        training.setDescription(request.getDescription());
        training.setInstitution(request.getInstitution());
        training.setCountry(request.getCountry());
    }
}
