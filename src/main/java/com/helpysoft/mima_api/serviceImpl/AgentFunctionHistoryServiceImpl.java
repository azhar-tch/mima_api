package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentFunctionHistoryRequest;
import com.helpysoft.mima_api.dto.AgentFunctionHistoryResponse;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.mapper.AgentFunctionHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentFunctionHistory;
import com.helpysoft.mima_api.entity.HRFunction;
import com.helpysoft.mima_api.repository.AgentFunctionHistoryRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.HRFunctionRepository;
import com.helpysoft.mima_api.service.AgentFunctionHistoryService;
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
public class AgentFunctionHistoryServiceImpl implements AgentFunctionHistoryService {
    private final AgentFunctionHistoryRepository agentFunctionHistoryRepository;
    private final AgentsRepository agentRepository;
    private final HRFunctionRepository hrFunctionRepository;
    private final AgentFunctionHistoryMapper agentFunctionHistoryMapper;
    private final HistoriesService historiesService;

    @Override
    public AgentFunctionHistoryResponse create(AgentFunctionHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        HRFunction function = hrFunctionRepository.findByTrackingId(request.getFunctionTrackingId())
                .orElseThrow(() -> new RuntimeException("Function not found with trackingId: " + request.getFunctionTrackingId()));

        AgentFunctionHistory history = agentFunctionHistoryMapper.toEntity(request, agent, function);
        AgentFunctionHistory savedHistory = agentFunctionHistoryRepository.save(history);

        // Record in Histories audit table
        try {
            String functionSummary = String.format(
                    "{\"trackingId\":\"%s\",\"functionName\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\"}",
                    savedHistory.getTrackingId(),
                    function.getFunctionName(),
                    savedHistory.getStartDate(),
                    savedHistory.getEndDate()
            );

            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agent.getTrackingId());
            historyRequest.setEntityName("AGENT_FUNCTION");
            historyRequest.setEntityTrackingId(savedHistory.getTrackingId());
            historyRequest.setActionType(ActionType.CREATE);
            historyRequest.setChangesSummary(
                    "Nouvelle fonction: " + function.getFunctionName() + " du " + savedHistory.getStartDate() +
                    (savedHistory.getEndDate() != null ? " au " + savedHistory.getEndDate() : "")
            );
            historyRequest.setNewValue(functionSummary);

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

        return agentFunctionHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentFunctionHistoryResponse update(UUID trackingId, AgentFunctionHistoryRequest request) {
        AgentFunctionHistory history = agentFunctionHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent function history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        HRFunction newFunction = hrFunctionRepository.findByTrackingId(request.getFunctionTrackingId())
                .orElseThrow(() -> new RuntimeException("Function not found with trackingId: " + request.getFunctionTrackingId()));

        // Save old values for change detection
        String oldFunctionName = history.getFunction().getFunctionName();
        LocalDate oldStartDate = history.getStartDate();
        LocalDate oldEndDate = history.getEndDate();

        agentFunctionHistoryMapper.updateEntity(history, request, agent, newFunction);
        AgentFunctionHistory updatedHistory = agentFunctionHistoryRepository.save(history);

        // Detect and record changes in Histories
        try {
            StringBuilder changes = new StringBuilder();
            boolean hasChanges = false;

            if (!Objects.equals(oldFunctionName, newFunction.getFunctionName())) {
                changes.append("Fonction: '").append(oldFunctionName).append("' → '").append(newFunction.getFunctionName()).append("' | ");
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
                historyRequest.setEntityName("AGENT_FUNCTION");
                historyRequest.setEntityTrackingId(updatedHistory.getTrackingId());
                historyRequest.setActionType(ActionType.UPDATE);
                historyRequest.setChangesSummary(changesSummary);

                historiesService.create(historyRequest);
            }
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

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

        // Save information for history record before deletion
        UUID agentTrackingId = history.getAgent().getTrackingId();
        String functionName = history.getFunction().getFunctionName();
        LocalDate startDate = history.getStartDate();
        LocalDate endDate = history.getEndDate();

        agentFunctionHistoryRepository.delete(history);

        // Record deletion in Histories
        try {
            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agentTrackingId);
            historyRequest.setEntityName("AGENT_FUNCTION");
            historyRequest.setEntityTrackingId(trackingId);
            historyRequest.setActionType(ActionType.DELETE);
            historyRequest.setChangesSummary(
                    "Suppression de la fonction: " + functionName + " du " + startDate +
                    (endDate != null ? " au " + endDate : "")
            );

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }
    }
}
