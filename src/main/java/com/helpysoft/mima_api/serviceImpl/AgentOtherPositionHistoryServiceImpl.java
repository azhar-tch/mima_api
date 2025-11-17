package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentOtherPositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentOtherPositionHistoryResponse;
import com.helpysoft.mima_api.mapper.AgentOtherPositionHistoryMapper;
import com.helpysoft.mima_api.model.Agent;
import com.helpysoft.mima_api.model.AgentOtherPositionHistory;
import com.helpysoft.mima_api.model.OtherPosition;
import com.helpysoft.mima_api.repository.AgentOtherPositionHistoryRepository;
import com.helpysoft.mima_api.repository.AgentRepository;
import com.helpysoft.mima_api.repository.OtherPositionRepository;
import com.helpysoft.mima_api.service.AgentOtherPositionHistoryService;
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
public class AgentOtherPositionHistoryServiceImpl implements AgentOtherPositionHistoryService {
    private final AgentOtherPositionHistoryRepository agentOtherPositionHistoryRepository;
    private final AgentRepository agentRepository;
    private final OtherPositionRepository otherPositionRepository;
    private final AgentOtherPositionHistoryMapper agentOtherPositionHistoryMapper;

    @Override
    public AgentOtherPositionHistoryResponse create(AgentOtherPositionHistoryRequest request) {
        Agent agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        OtherPosition position = otherPositionRepository.findByTrackingId(request.getOtherPositionTrackingId())
                .orElseThrow(() -> new RuntimeException("Other position not found with trackingId: " + request.getOtherPositionTrackingId()));

        AgentOtherPositionHistory history = agentOtherPositionHistoryMapper.toEntity(request, agent, position);
        AgentOtherPositionHistory savedHistory = agentOtherPositionHistoryRepository.save(history);
        return agentOtherPositionHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentOtherPositionHistoryResponse update(UUID trackingId, AgentOtherPositionHistoryRequest request) {
        AgentOtherPositionHistory history = agentOtherPositionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent other position history not found with trackingId: " + trackingId));
        Agent agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        OtherPosition position = otherPositionRepository.findByTrackingId(request.getOtherPositionTrackingId())
                .orElseThrow(() -> new RuntimeException("Other position not found with trackingId: " + request.getOtherPositionTrackingId()));

        agentOtherPositionHistoryMapper.updateEntity(history, request, agent, position);
        AgentOtherPositionHistory updatedHistory = agentOtherPositionHistoryRepository.save(history);
        return agentOtherPositionHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentOtherPositionHistoryResponse findByTrackingId(UUID trackingId) {
        AgentOtherPositionHistory history = agentOtherPositionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent other position history not found with trackingId: " + trackingId));
        return agentOtherPositionHistoryMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentOtherPositionHistoryResponse> findByAgentTrackingId(UUID agentTrackingId) {
        return agentOtherPositionHistoryRepository.findByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentOtherPositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentOtherPositionHistoryResponse> findByOtherPositionTrackingId(UUID positionTrackingId) {
        return agentOtherPositionHistoryRepository.findByOtherPositionTrackingId(positionTrackingId)
                .stream()
                .map(agentOtherPositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentOtherPositionHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return agentOtherPositionHistoryRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(agentOtherPositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentOtherPositionHistoryResponse> findOngoingPositionsByAgentTrackingId(UUID agentTrackingId) {
        return agentOtherPositionHistoryRepository.findOngoingPositionsByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentOtherPositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentOtherPositionHistoryResponse> findAll() {
        return agentOtherPositionHistoryRepository.findAll()
                .stream()
                .map(agentOtherPositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        AgentOtherPositionHistory history = agentOtherPositionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent other position history not found with trackingId: " + trackingId));
        agentOtherPositionHistoryRepository.delete(history);
    }
}
