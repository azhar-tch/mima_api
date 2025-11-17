package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentAwardHistoryRequest;
import com.helpysoft.mima_api.dto.AgentAwardHistoryResponse;
import com.helpysoft.mima_api.mapper.AgentAwardHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentAwardHistory;
import com.helpysoft.mima_api.entity.Award;
import com.helpysoft.mima_api.repository.AgentAwardHistoryRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.AwardRepository;
import com.helpysoft.mima_api.service.AgentAwardHistoryService;
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
public class AgentAwardHistoryServiceImpl implements AgentAwardHistoryService {
    private final AgentAwardHistoryRepository agentAwardHistoryRepository;
    private final AgentsRepository agentRepository;
    private final AwardRepository awardRepository;
    private final AgentAwardHistoryMapper agentAwardHistoryMapper;

    @Override
    public AgentAwardHistoryResponse create(AgentAwardHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        Award award = awardRepository.findByTrackingId(request.getAwardTrackingId())
                .orElseThrow(() -> new RuntimeException("Award not found with trackingId: " + request.getAwardTrackingId()));

        AgentAwardHistory history = agentAwardHistoryMapper.toEntity(request, agent, award);
        AgentAwardHistory savedHistory = agentAwardHistoryRepository.save(history);
        return agentAwardHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentAwardHistoryResponse update(UUID trackingId, AgentAwardHistoryRequest request) {
        AgentAwardHistory history = agentAwardHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent award history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        Award award = awardRepository.findByTrackingId(request.getAwardTrackingId())
                .orElseThrow(() -> new RuntimeException("Award not found with trackingId: " + request.getAwardTrackingId()));

        agentAwardHistoryMapper.updateEntity(history, request, agent, award);
        AgentAwardHistory updatedHistory = agentAwardHistoryRepository.save(history);
        return agentAwardHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentAwardHistoryResponse findByTrackingId(UUID trackingId) {
        AgentAwardHistory history = agentAwardHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent award history not found with trackingId: " + trackingId));
        return agentAwardHistoryMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentAwardHistoryResponse> findByAgentTrackingId(UUID agentTrackingId) {
        return agentAwardHistoryRepository.findByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentAwardHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentAwardHistoryResponse> findByAwardTrackingId(UUID awardTrackingId) {
        return agentAwardHistoryRepository.findByAwardTrackingId(awardTrackingId)
                .stream()
                .map(agentAwardHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentAwardHistoryResponse> findByAwardDateBetween(LocalDate startDate, LocalDate endDate) {
        return agentAwardHistoryRepository.findByAwardDateBetween(startDate, endDate)
                .stream()
                .map(agentAwardHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByAgentTrackingId(UUID agentTrackingId) {
        return agentAwardHistoryRepository.countByAgentTrackingId(agentTrackingId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentAwardHistoryResponse> findAll() {
        return agentAwardHistoryRepository.findAll()
                .stream()
                .map(agentAwardHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        AgentAwardHistory history = agentAwardHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent award history not found with trackingId: " + trackingId));
        agentAwardHistoryRepository.delete(history);
    }
}
