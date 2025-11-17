package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.TrainingRequest;
import com.helpysoft.mima_api.dto.TrainingResponse;
import com.helpysoft.mima_api.mapper.TrainingMapper;
import com.helpysoft.mima_api.entity.Training;
import com.helpysoft.mima_api.repository.TrainingRepository;
import com.helpysoft.mima_api.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;

    @Override
    public TrainingResponse create(TrainingRequest request) {
        if (trainingRepository.existsByTrainingName(request.getTrainingName())) {
            throw new RuntimeException("Training with name '" + request.getTrainingName() + "' already exists");
        }
        Training training = trainingMapper.toEntity(request);
        Training savedTraining = trainingRepository.save(training);
        return trainingMapper.toResponse(savedTraining);
    }

    @Override
    public TrainingResponse update(UUID trackingId, TrainingRequest request) {
        Training training = trainingRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Training not found with trackingId: " + trackingId));
        trainingMapper.updateEntity(training, request);
        Training updatedTraining = trainingRepository.save(training);
        return trainingMapper.toResponse(updatedTraining);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingResponse findByTrackingId(UUID trackingId) {
        Training training = trainingRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Training not found with trackingId: " + trackingId));
        return trainingMapper.toResponse(training);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingResponse findByTrainingName(String trainingName) {
        Training training = trainingRepository.findByTrainingName(trainingName)
                .orElseThrow(() -> new RuntimeException("Training not found with name: " + trainingName));
        return trainingMapper.toResponse(training);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingResponse> findByTrainingType(String trainingType) {
        return trainingRepository.findByTrainingType(trainingType)
                .stream()
                .map(trainingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingResponse> findByCountry(String country) {
        return trainingRepository.findByCountry(country)
                .stream()
                .map(trainingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingResponse> findByInstitution(String institution) {
        return trainingRepository.findByInstitution(institution)
                .stream()
                .map(trainingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingResponse> searchByName(String trainingName) {
        return trainingRepository.findByTrainingNameContainingIgnoreCase(trainingName)
                .stream()
                .map(trainingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingResponse> findAll() {
        return trainingRepository.findAll()
                .stream()
                .map(trainingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Training training = trainingRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Training not found with trackingId: " + trackingId));
        trainingRepository.delete(training);
    }
}
