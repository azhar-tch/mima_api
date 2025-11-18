package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentAwardHistoryRequest;
import com.helpysoft.mima_api.dto.AgentAwardHistoryResponse;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.entity.ActionType;
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
import java.util.Objects;
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
    private final HistoriesService historiesService;

    @Override
    public AgentAwardHistoryResponse create(AgentAwardHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        Award award = awardRepository.findByTrackingId(request.getAwardTrackingId())
                .orElseThrow(() -> new RuntimeException("Award not found with trackingId: " + request.getAwardTrackingId()));

        AgentAwardHistory history = agentAwardHistoryMapper.toEntity(request, agent, award);
        AgentAwardHistory savedHistory = agentAwardHistoryRepository.save(history);

        // Record in Histories audit table
        try {
            String awardSummary = String.format(
                    "{\"trackingId\":\"%s\",\"awardName\":\"%s\",\"awardDate\":\"%s\"}",
                    savedHistory.getTrackingId(),
                    award.getAwardName(),
                    savedHistory.getAwardDate()
            );

            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agent.getTrackingId());
            historyRequest.setEntityName("AGENT_AWARD");
            historyRequest.setEntityTrackingId(savedHistory.getTrackingId());
            historyRequest.setActionType(ActionType.CREATE);
            historyRequest.setChangesSummary(
                    "Nouvelle distinction attribuée: " + award.getAwardName() + " le " + savedHistory.getAwardDate()
            );
            historyRequest.setNewValue(awardSummary);

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

        return agentAwardHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentAwardHistoryResponse update(UUID trackingId, AgentAwardHistoryRequest request) {
        AgentAwardHistory history = agentAwardHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent award history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        Award newAward = awardRepository.findByTrackingId(request.getAwardTrackingId())
                .orElseThrow(() -> new RuntimeException("Award not found with trackingId: " + request.getAwardTrackingId()));

        // Save old values for change detection
        String oldAwardName = history.getAward().getAwardName();
        LocalDate oldAwardDate = history.getAwardDate();

        agentAwardHistoryMapper.updateEntity(history, request, agent, newAward);
        AgentAwardHistory updatedHistory = agentAwardHistoryRepository.save(history);

        // Detect and record changes in Histories
        try {
            StringBuilder changes = new StringBuilder();
            boolean hasChanges = false;

            if (!Objects.equals(oldAwardName, newAward.getAwardName())) {
                changes.append("Distinction: '").append(oldAwardName).append("' → '").append(newAward.getAwardName()).append("' | ");
                hasChanges = true;
            }

            if (!Objects.equals(oldAwardDate, request.getAwardDate())) {
                changes.append("Date: '").append(oldAwardDate).append("' → '").append(request.getAwardDate()).append("' | ");
                hasChanges = true;
            }

            if (hasChanges) {
                String changesSummary = changes.substring(0, changes.length() - 3);

                HistoriesRequest historyRequest = new HistoriesRequest();
                historyRequest.setAgentTrackingId(agent.getTrackingId());
                historyRequest.setEntityName("AGENT_AWARD");
                historyRequest.setEntityTrackingId(updatedHistory.getTrackingId());
                historyRequest.setActionType(ActionType.UPDATE);
                historyRequest.setChangesSummary(changesSummary);

                historiesService.create(historyRequest);
            }
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

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

        // Save information for history record before deletion
        UUID agentTrackingId = history.getAgent().getTrackingId();
        String awardName = history.getAward().getAwardName();
        LocalDate awardDate = history.getAwardDate();

        agentAwardHistoryRepository.delete(history);

        // Record deletion in Histories
        try {
            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agentTrackingId);
            historyRequest.setEntityName("AGENT_AWARD");
            historyRequest.setEntityTrackingId(trackingId);
            historyRequest.setActionType(ActionType.DELETE);
            historyRequest.setChangesSummary(
                    "Suppression de la distinction: " + awardName + " du " + awardDate
            );

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }
    }
}
