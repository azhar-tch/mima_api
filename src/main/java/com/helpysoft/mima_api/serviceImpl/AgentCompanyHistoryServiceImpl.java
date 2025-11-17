package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentCompanyHistoryRequest;
import com.helpysoft.mima_api.dto.AgentCompanyHistoryResponse;
import com.helpysoft.mima_api.mapper.AgentCompanyHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentCompanyHistory;
import com.helpysoft.mima_api.entity.BMLCompany;
import com.helpysoft.mima_api.repository.AgentCompanyHistoryRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.BMLCompanyRepository;
import com.helpysoft.mima_api.service.AgentCompanyHistoryService;
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
public class AgentCompanyHistoryServiceImpl implements AgentCompanyHistoryService {
    private final AgentCompanyHistoryRepository agentCompanyHistoryRepository;
    private final AgentsRepository agentRepository;
    private final BMLCompanyRepository bmlCompanyRepository;
    private final AgentCompanyHistoryMapper agentCompanyHistoryMapper;

    @Override
    public AgentCompanyHistoryResponse create(AgentCompanyHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        BMLCompany company = bmlCompanyRepository.findByTrackingId(request.getCompanyTrackingId())
                .orElseThrow(() -> new RuntimeException("Company not found with trackingId: " + request.getCompanyTrackingId()));

        AgentCompanyHistory history = agentCompanyHistoryMapper.toEntity(request, agent, company);
        AgentCompanyHistory savedHistory = agentCompanyHistoryRepository.save(history);
        return agentCompanyHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentCompanyHistoryResponse update(UUID trackingId, AgentCompanyHistoryRequest request) {
        AgentCompanyHistory history = agentCompanyHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent company history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        BMLCompany company = bmlCompanyRepository.findByTrackingId(request.getCompanyTrackingId())
                .orElseThrow(() -> new RuntimeException("Company not found with trackingId: " + request.getCompanyTrackingId()));

        agentCompanyHistoryMapper.updateEntity(history, request, agent, company);
        AgentCompanyHistory updatedHistory = agentCompanyHistoryRepository.save(history);
        return agentCompanyHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentCompanyHistoryResponse findByTrackingId(UUID trackingId) {
        AgentCompanyHistory history = agentCompanyHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent company history not found with trackingId: " + trackingId));
        return agentCompanyHistoryMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentCompanyHistoryResponse> findByAgentTrackingId(UUID agentTrackingId) {
        return agentCompanyHistoryRepository.findByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentCompanyHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentCompanyHistoryResponse> findByCompanyTrackingId(UUID companyTrackingId) {
        return agentCompanyHistoryRepository.findByCompanyTrackingId(companyTrackingId)
                .stream()
                .map(agentCompanyHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentCompanyHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return agentCompanyHistoryRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(agentCompanyHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AgentCompanyHistoryResponse findCurrentCompanyByAgentTrackingId(UUID agentTrackingId) {
        return agentCompanyHistoryRepository.findCurrentCompanyByAgentTrackingId(agentTrackingId)
                .map(agentCompanyHistoryMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("No current company found for agent with trackingId: " + agentTrackingId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentCompanyHistoryResponse> findCurrentMembersByCompanyTrackingId(UUID companyTrackingId) {
        return agentCompanyHistoryRepository.findCurrentMembersByCompanyTrackingId(companyTrackingId)
                .stream()
                .map(agentCompanyHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentCompanyHistoryResponse> findAll() {
        return agentCompanyHistoryRepository.findAll()
                .stream()
                .map(agentCompanyHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        AgentCompanyHistory history = agentCompanyHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent company history not found with trackingId: " + trackingId));
        agentCompanyHistoryRepository.delete(history);
    }
}
