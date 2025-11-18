package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentOtherPositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentOtherPositionHistoryResponse;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.mapper.AgentOtherPositionHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentOtherPositionHistory;
import com.helpysoft.mima_api.entity.OtherPosition;
import com.helpysoft.mima_api.repository.AgentOtherPositionHistoryRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.OtherPositionRepository;
import com.helpysoft.mima_api.service.AgentOtherPositionHistoryService;
import com.helpysoft.mima_api.service.HistoriesService;
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
public class AgentOtherPositionHistoryServiceImpl implements AgentOtherPositionHistoryService {
    private final AgentOtherPositionHistoryRepository agentOtherPositionHistoryRepository;
    private final AgentsRepository agentRepository;
    private final OtherPositionRepository otherPositionRepository;
    private final AgentOtherPositionHistoryMapper agentOtherPositionHistoryMapper;
    private final HistoriesService historiesService;

    @Override
    public AgentOtherPositionHistoryResponse create(AgentOtherPositionHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        OtherPosition position = otherPositionRepository.findByTrackingId(request.getOtherPositionTrackingId())
                .orElseThrow(() -> new RuntimeException("Other position not found with trackingId: " + request.getOtherPositionTrackingId()));

        AgentOtherPositionHistory history = agentOtherPositionHistoryMapper.toEntity(request, agent, position);
        AgentOtherPositionHistory savedHistory = agentOtherPositionHistoryRepository.save(history);

        // Record in Histories audit table
        try {
            String positionSummary = String.format(
                    "{\"trackingId\":\"%s\",\"positionName\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\"}",
                    savedHistory.getTrackingId(),
                    position.getPositionName(),
                    savedHistory.getStartDate(),
                    savedHistory.getEndDate()
            );

            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agent.getTrackingId());
            historyRequest.setEntityName("AGENT_OTHER_POSITION");
            historyRequest.setEntityTrackingId(savedHistory.getTrackingId());
            historyRequest.setActionType(ActionType.CREATE);
            historyRequest.setChangesSummary(
                    "Nouvelle autre position: " + position.getPositionName() + " du " + savedHistory.getStartDate() +
                    (savedHistory.getEndDate() != null ? " au " + savedHistory.getEndDate() : "")
            );
            historyRequest.setNewValue(positionSummary);

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

        return agentOtherPositionHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentOtherPositionHistoryResponse update(UUID trackingId, AgentOtherPositionHistoryRequest request) {
        AgentOtherPositionHistory history = agentOtherPositionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent other position history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        OtherPosition newPosition = otherPositionRepository.findByTrackingId(request.getOtherPositionTrackingId())
                .orElseThrow(() -> new RuntimeException("Other position not found with trackingId: " + request.getOtherPositionTrackingId()));

        // Save old values for change detection
        String oldPositionName = history.getOtherPosition().getPositionName();
        LocalDate oldStartDate = history.getStartDate();
        LocalDate oldEndDate = history.getEndDate();

        agentOtherPositionHistoryMapper.updateEntity(history, request, agent, newPosition);
        AgentOtherPositionHistory updatedHistory = agentOtherPositionHistoryRepository.save(history);

        // Detect and record changes in Histories
        try {
            StringBuilder changes = new StringBuilder();
            boolean hasChanges = false;

            if (!Objects.equals(oldPositionName, newPosition.getPositionName())) {
                changes.append("Autre position: '").append(oldPositionName).append("' → '").append(newPosition.getPositionName()).append("' | ");
                hasChanges = true;
            }

            if (!Objects.equals(oldStartDate, request.getStartDate())) {
                changes.append("Date début: '").append(oldStartDate).append("' → '").append(request.getStartDate()).append("' | ");
                hasChanges = true;
            }

            if (!Objects.equals(oldEndDate, request.getEndDate())) {
                changes.append("Date fin: '").append(oldEndDate).append("' → '").append(request.getEndDate()).append("' | ");
                hasChanges = true;
            }

            if (hasChanges) {
                String changesSummary = changes.substring(0, changes.length() - 3);

                HistoriesRequest historyRequest = new HistoriesRequest();
                historyRequest.setAgentTrackingId(agent.getTrackingId());
                historyRequest.setEntityName("AGENT_OTHER_POSITION");
                historyRequest.setEntityTrackingId(updatedHistory.getTrackingId());
                historyRequest.setActionType(ActionType.UPDATE);
                historyRequest.setChangesSummary(changesSummary);

                historiesService.create(historyRequest);
            }
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

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

        // Save information for history record before deletion
        UUID agentTrackingId = history.getAgent().getTrackingId();
        String positionName = history.getOtherPosition().getPositionName();
        LocalDate startDate = history.getStartDate();
        LocalDate endDate = history.getEndDate();

        agentOtherPositionHistoryRepository.delete(history);

        // Record deletion in Histories
        try {
            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agentTrackingId);
            historyRequest.setEntityName("AGENT_OTHER_POSITION");
            historyRequest.setEntityTrackingId(trackingId);
            historyRequest.setActionType(ActionType.DELETE);
            historyRequest.setChangesSummary(
                    "Suppression de l'autre position: " + positionName + " du " + startDate +
                    (endDate != null ? " au " + endDate : "")
            );

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }
    }
}
