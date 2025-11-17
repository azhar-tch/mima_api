package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentGradeHistoryRequest;
import com.helpysoft.mima_api.dto.AgentGradeHistoryResponse;
import com.helpysoft.mima_api.mapper.AgentGradeHistoryMapper;
import com.helpysoft.mima_api.model.Agent;
import com.helpysoft.mima_api.model.AgentGradeHistory;
import com.helpysoft.mima_api.model.HRGrade;
import com.helpysoft.mima_api.repository.AgentGradeHistoryRepository;
import com.helpysoft.mima_api.repository.AgentRepository;
import com.helpysoft.mima_api.repository.HRGradeRepository;
import com.helpysoft.mima_api.service.AgentGradeHistoryService;
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
public class AgentGradeHistoryServiceImpl implements AgentGradeHistoryService {
    private final AgentGradeHistoryRepository agentGradeHistoryRepository;
    private final AgentRepository agentRepository;
    private final HRGradeRepository hrGradeRepository;
    private final AgentGradeHistoryMapper agentGradeHistoryMapper;

    @Override
    public AgentGradeHistoryResponse create(AgentGradeHistoryRequest request) {
        Agent agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        HRGrade grade = hrGradeRepository.findByTrackingId(request.getGradeTrackingId())
                .orElseThrow(() -> new RuntimeException("Grade not found with trackingId: " + request.getGradeTrackingId()));

        AgentGradeHistory history = agentGradeHistoryMapper.toEntity(request, agent, grade);
        AgentGradeHistory savedHistory = agentGradeHistoryRepository.save(history);
        return agentGradeHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentGradeHistoryResponse update(UUID trackingId, AgentGradeHistoryRequest request) {
        AgentGradeHistory history = agentGradeHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent grade history not found with trackingId: " + trackingId));
        Agent agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        HRGrade grade = hrGradeRepository.findByTrackingId(request.getGradeTrackingId())
                .orElseThrow(() -> new RuntimeException("Grade not found with trackingId: " + request.getGradeTrackingId()));

        agentGradeHistoryMapper.updateEntity(history, request, agent, grade);
        AgentGradeHistory updatedHistory = agentGradeHistoryRepository.save(history);
        return agentGradeHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentGradeHistoryResponse findByTrackingId(UUID trackingId) {
        AgentGradeHistory history = agentGradeHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent grade history not found with trackingId: " + trackingId));
        return agentGradeHistoryMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentGradeHistoryResponse> findByAgentTrackingId(UUID agentTrackingId) {
        return agentGradeHistoryRepository.findByAgentTrackingIdOrderByPromotionDateDesc(agentTrackingId)
                .stream()
                .map(agentGradeHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentGradeHistoryResponse> findByGradeTrackingId(UUID gradeTrackingId) {
        return agentGradeHistoryRepository.findByGradeTrackingId(gradeTrackingId)
                .stream()
                .map(agentGradeHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentGradeHistoryResponse> findByPromotionDateBetween(LocalDate startDate, LocalDate endDate) {
        return agentGradeHistoryRepository.findByPromotionDateBetween(startDate, endDate)
                .stream()
                .map(agentGradeHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AgentGradeHistoryResponse findLatestGradeByAgentTrackingId(UUID agentTrackingId) {
        return agentGradeHistoryRepository.findLatestGradeByAgentTrackingId(agentTrackingId)
                .map(agentGradeHistoryMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("No grade history found for agent with trackingId: " + agentTrackingId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentGradeHistoryResponse> findAll() {
        return agentGradeHistoryRepository.findAll()
                .stream()
                .map(agentGradeHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        AgentGradeHistory history = agentGradeHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent grade history not found with trackingId: " + trackingId));
        agentGradeHistoryRepository.delete(history);
    }
}
