package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentServicePositionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentServicePositionHistoryResponse;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.mapper.AgentServicePositionHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentServicePositionHistory;
import com.helpysoft.mima_api.entity.ServicePosition;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.AgentServicePositionHistoryRepository;
import com.helpysoft.mima_api.repository.ServicePositionRepository;
import com.helpysoft.mima_api.service.AgentServicePositionHistoryService;
import com.helpysoft.mima_api.serviceImpl.HistoriesService;
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
public class AgentServicePositionHistoryServiceImpl implements AgentServicePositionHistoryService {
    private final AgentServicePositionHistoryRepository agentServicePositionHistoryRepository;
    private final AgentsRepository agentRepository;
    private final ServicePositionRepository servicePositionRepository;
    private final AgentServicePositionHistoryMapper agentServicePositionHistoryMapper;
    private final HistoriesService historiesService;

    @Override
    public AgentServicePositionHistoryResponse create(AgentServicePositionHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        ServicePosition position = servicePositionRepository.findByTrackingId(request.getServicePositionTrackingId())
                .orElseThrow(() -> new RuntimeException("Service position not found with trackingId: " + request.getServicePositionTrackingId()));

        AgentServicePositionHistory history = agentServicePositionHistoryMapper.toEntity(request, agent, position);
        AgentServicePositionHistory savedHistory = agentServicePositionHistoryRepository.save(history);

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
            historyRequest.setEntityName("AGENT_SERVICE_POSITION");
            historyRequest.setEntityTrackingId(savedHistory.getTrackingId());
            historyRequest.setActionType(ActionType.CREATE);
            historyRequest.setChangesSummary(
                    "Nouvelle position service: " + position.getPositionName() + " du " + savedHistory.getStartDate() +
                    (savedHistory.getEndDate() != null ? " au " + savedHistory.getEndDate() : "")
            );
            historyRequest.setNewValue(positionSummary);

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

        return agentServicePositionHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentServicePositionHistoryResponse update(UUID trackingId, AgentServicePositionHistoryRequest request) {
        AgentServicePositionHistory history = agentServicePositionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent service position history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        ServicePosition newPosition = servicePositionRepository.findByTrackingId(request.getServicePositionTrackingId())
                .orElseThrow(() -> new RuntimeException("Service position not found with trackingId: " + request.getServicePositionTrackingId()));

        // Save old values for change detection
        String oldPositionName = history.getServicePosition().getPositionName();
        LocalDate oldStartDate = history.getStartDate();
        LocalDate oldEndDate = history.getEndDate();

        agentServicePositionHistoryMapper.updateEntity(history, request, agent, newPosition);
        AgentServicePositionHistory updatedHistory = agentServicePositionHistoryRepository.save(history);

        // Detect and record changes in Histories
        try {
            StringBuilder changes = new StringBuilder();
            boolean hasChanges = false;

            if (!Objects.equals(oldPositionName, newPosition.getPositionName())) {
                changes.append("Position service: '").append(oldPositionName).append("' → '").append(newPosition.getPositionName()).append("' | ");
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
                historyRequest.setEntityName("AGENT_SERVICE_POSITION");
                historyRequest.setEntityTrackingId(updatedHistory.getTrackingId());
                historyRequest.setActionType(ActionType.UPDATE);
                historyRequest.setChangesSummary(changesSummary);

                historiesService.create(historyRequest);
            }
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

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

        // Save information for history record before deletion
        UUID agentTrackingId = history.getAgent().getTrackingId();
        String positionName = history.getServicePosition().getPositionName();
        LocalDate startDate = history.getStartDate();
        LocalDate endDate = history.getEndDate();

        agentServicePositionHistoryRepository.delete(history);

        // Record deletion in Histories
        try {
            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agentTrackingId);
            historyRequest.setEntityName("AGENT_SERVICE_POSITION");
            historyRequest.setEntityTrackingId(trackingId);
            historyRequest.setActionType(ActionType.DELETE);
            historyRequest.setChangesSummary(
                    "Suppression de la position service: " + positionName + " du " + startDate +
                    (endDate != null ? " au " + endDate : "")
            );

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }
    }
}
