package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentServicePositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentServicePositionHistoryResponse;
import com.helpysoft.mima_api.mapper.AgentServicePositionHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentServicePositionHistory;
import com.helpysoft.mima_api.entity.ServicePosition;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.AgentServicePositionHistoryRepository;
import com.helpysoft.mima_api.repository.ServicePositionRepository;
import com.helpysoft.mima_api.service.AgentServicePositionHistoryService;
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
public class AgentServicePositionHistoryServiceImpl implements AgentServicePositionHistoryService {
    private final AgentServicePositionHistoryRepository agentServicePositionHistoryRepository;
    private final AgentsRepository agentRepository;
    private final ServicePositionRepository servicePositionRepository;
    private final AgentServicePositionHistoryMapper agentServicePositionHistoryMapper;

    @Override
    public AgentServicePositionHistoryResponse create(AgentServicePositionHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        ServicePosition position = servicePositionRepository.findByTrackingId(request.getServicePositionTrackingId())
                .orElseThrow(() -> new RuntimeException("Service position not found with trackingId: " + request.getServicePositionTrackingId()));

        AgentServicePositionHistory history = agentServicePositionHistoryMapper.toEntity(request, agent, position);
        AgentServicePositionHistory savedHistory = agentServicePositionHistoryRepository.save(history);
        return agentServicePositionHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentServicePositionHistoryResponse update(UUID trackingId, AgentServicePositionHistoryRequest request) {
        AgentServicePositionHistory history = agentServicePositionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent service position history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        ServicePosition position = servicePositionRepository.findByTrackingId(request.getServicePositionTrackingId())
                .orElseThrow(() -> new RuntimeException("Service position not found with trackingId: " + request.getServicePositionTrackingId()));

        agentServicePositionHistoryMapper.updateEntity(history, request, agent, position);
        AgentServicePositionHistory updatedHistory = agentServicePositionHistoryRepository.save(history);
        return agentServicePositionHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentServicePositionHistoryResponse findByTrackingId(UUID trackingId) {
        AgentServicePositionHistory history = agentServicePositionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent service position history not found with trackingId: " + trackingId));
        return agentServicePositionHistoryMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentServicePositionHistoryResponse> findByAgentTrackingId(UUID agentTrackingId) {
        return agentServicePositionHistoryRepository.findByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentServicePositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentServicePositionHistoryResponse> findByServicePositionTrackingId(UUID positionTrackingId) {
        return agentServicePositionHistoryRepository.findByServicePositionTrackingId(positionTrackingId)
                .stream()
                .map(agentServicePositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentServicePositionHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return agentServicePositionHistoryRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(agentServicePositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AgentServicePositionHistoryResponse findCurrentPositionByAgentTrackingId(UUID agentTrackingId) {
        return agentServicePositionHistoryRepository.findCurrentPositionByAgentTrackingId(agentTrackingId)
                .map(agentServicePositionHistoryMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("No current service position found for agent with trackingId: " + agentTrackingId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentServicePositionHistoryResponse> findAll() {
        return agentServicePositionHistoryRepository.findAll()
                .stream()
                .map(agentServicePositionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        AgentServicePositionHistory history = agentServicePositionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent service position history not found with trackingId: " + trackingId));
        agentServicePositionHistoryRepository.delete(history);
    }
}
