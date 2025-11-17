package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentTrainingHistoryRequest;
import com.helpysoft.mima_api.dto.AgentTrainingHistoryResponse;
import com.helpysoft.mima_api.mapper.AgentTrainingHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentTrainingHistory;
import com.helpysoft.mima_api.entity.Training;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.AgentTrainingHistoryRepository;
import com.helpysoft.mima_api.repository.TrainingRepository;
import com.helpysoft.mima_api.service.AgentTrainingHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentTrainingHistoryServiceImpl implements AgentTrainingHistoryService {
    private final AgentTrainingHistoryRepository agentTrainingHistoryRepository;
    private final AgentsRepository agentRepository;
    private final TrainingRepository trainingRepository;
    private final AgentTrainingHistoryMapper agentTrainingHistoryMapper;

    @Override
    public AgentTrainingHistoryResponse create(AgentTrainingHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        Training training = trainingRepository.findByTrackingId(request.getTrainingTrackingId())
                .orElseThrow(() -> new RuntimeException("Training not found with trackingId: " + request.getTrainingTrackingId()));

        AgentTrainingHistory history = agentTrainingHistoryMapper.toEntity(request, agent, training);
        AgentTrainingHistory savedHistory = agentTrainingHistoryRepository.save(history);
        return agentTrainingHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentTrainingHistoryResponse update(UUID trackingId, AgentTrainingHistoryRequest request) {
        AgentTrainingHistory history = agentTrainingHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent training history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        Training training = trainingRepository.findByTrackingId(request.getTrainingTrackingId())
                .orElseThrow(() -> new RuntimeException("Training not found with trackingId: " + request.getTrainingTrackingId()));

        agentTrainingHistoryMapper.updateEntity(history, request, agent, training);
        AgentTrainingHistory updatedHistory = agentTrainingHistoryRepository.save(history);
        return agentTrainingHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentTrainingHistoryResponse findByTrackingId(UUID trackingId) {
        AgentTrainingHistory history = agentTrainingHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent training history not found with trackingId: " + trackingId));
        return agentTrainingHistoryMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findByAgentTrackingId(UUID agentTrackingId) {
        return agentTrainingHistoryRepository.findByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findByTrainingTrackingId(UUID trainingTrackingId) {
        return agentTrainingHistoryRepository.findByTrainingTrackingId(trainingTrackingId)
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return agentTrainingHistoryRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findOngoingTrainingsByAgentTrackingId(UUID agentTrackingId) {
        return agentTrainingHistoryRepository.findOngoingTrainingsByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findAll() {
        return agentTrainingHistoryRepository.findAll()
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        AgentTrainingHistory history = agentTrainingHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent training history not found with trackingId: " + trackingId));
        agentTrainingHistoryRepository.delete(history);
    }
}
