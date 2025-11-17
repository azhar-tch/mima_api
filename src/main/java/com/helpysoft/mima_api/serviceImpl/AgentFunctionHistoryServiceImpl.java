package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentFunctionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentFunctionHistoryResponse;
import com.helpysoft.mima_api.mapper.AgentFunctionHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentFunctionHistory;
import com.helpysoft.mima_api.entity.HRFunction;
import com.helpysoft.mima_api.repository.AgentFunctionHistoryRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.HRFunctionRepository;
import com.helpysoft.mima_api.service.AgentFunctionHistoryService;
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
public class AgentFunctionHistoryServiceImpl implements AgentFunctionHistoryService {
    private final AgentFunctionHistoryRepository agentFunctionHistoryRepository;
    private final AgentsRepository agentRepository;
    private final HRFunctionRepository hrFunctionRepository;
    private final AgentFunctionHistoryMapper agentFunctionHistoryMapper;

    @Override
    public AgentFunctionHistoryResponse create(AgentFunctionHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        HRFunction function = hrFunctionRepository.findByTrackingId(request.getFunctionTrackingId())
                .orElseThrow(() -> new RuntimeException("Function not found with trackingId: " + request.getFunctionTrackingId()));

        AgentFunctionHistory history = agentFunctionHistoryMapper.toEntity(request, agent, function);
        AgentFunctionHistory savedHistory = agentFunctionHistoryRepository.save(history);
        return agentFunctionHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentFunctionHistoryResponse update(UUID trackingId, AgentFunctionHistoryRequest request) {
        AgentFunctionHistory history = agentFunctionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent function history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        HRFunction function = hrFunctionRepository.findByTrackingId(request.getFunctionTrackingId())
                .orElseThrow(() -> new RuntimeException("Function not found with trackingId: " + request.getFunctionTrackingId()));

        agentFunctionHistoryMapper.updateEntity(history, request, agent, function);
        AgentFunctionHistory updatedHistory = agentFunctionHistoryRepository.save(history);
        return agentFunctionHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentFunctionHistoryResponse findByTrackingId(UUID trackingId) {
        AgentFunctionHistory history = agentFunctionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent function history not found with trackingId: " + trackingId));
        return agentFunctionHistoryMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentFunctionHistoryResponse> findByAgentTrackingId(UUID agentTrackingId) {
        return agentFunctionHistoryRepository.findByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentFunctionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentFunctionHistoryResponse> findByFunctionTrackingId(UUID functionTrackingId) {
        return agentFunctionHistoryRepository.findByFunctionTrackingId(functionTrackingId)
                .stream()
                .map(agentFunctionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentFunctionHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return agentFunctionHistoryRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(agentFunctionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AgentFunctionHistoryResponse findCurrentFunctionByAgentTrackingId(UUID agentTrackingId) {
        return agentFunctionHistoryRepository.findCurrentFunctionByAgentTrackingId(agentTrackingId)
                .map(agentFunctionHistoryMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("No current function found for agent with trackingId: " + agentTrackingId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentFunctionHistoryResponse> findAll() {
        return agentFunctionHistoryRepository.findAll()
                .stream()
                .map(agentFunctionHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        AgentFunctionHistory history = agentFunctionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent function history not found with trackingId: " + trackingId));
        agentFunctionHistoryRepository.delete(history);
    }
}
