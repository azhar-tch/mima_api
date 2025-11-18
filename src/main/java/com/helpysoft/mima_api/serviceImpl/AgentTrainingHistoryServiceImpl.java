package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentTrainingHistoryRequest;
import com.helpysoft.mima_api.dto.AgentTrainingHistoryResponse;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.mapper.AgentTrainingHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentTrainingHistory;
import com.helpysoft.mima_api.entity.Training;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.AgentTrainingHistoryRepository;
import com.helpysoft.mima_api.repository.TrainingRepository;
import com.helpysoft.mima_api.service.AgentTrainingHistoryService;
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
public class AgentTrainingHistoryServiceImpl implements AgentTrainingHistoryService {
    private final AgentTrainingHistoryRepository agentTrainingHistoryRepository;
    private final AgentsRepository agentRepository;
    private final TrainingRepository trainingRepository;
    private final AgentTrainingHistoryMapper agentTrainingHistoryMapper;
    private final HistoriesService historiesService;

    @Override
    public AgentTrainingHistoryResponse create(AgentTrainingHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        Training training = trainingRepository.findByTrackingId(request.getTrainingTrackingId())
                .orElseThrow(() -> new RuntimeException("Training not found with trackingId: " + request.getTrainingTrackingId()));

        AgentTrainingHistory history = agentTrainingHistoryMapper.toEntity(request, agent, training);
        AgentTrainingHistory savedHistory = agentTrainingHistoryRepository.save(history);

        // Record in Histories audit table
        try {
            String trainingSummary = String.format(
                    "{\"trackingId\":\"%s\",\"trainingName\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\"}",
                    savedHistory.getTrackingId(),
                    training.getTrainingName(),
                    savedHistory.getStartDate(),
                    savedHistory.getEndDate()
            );

            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agent.getTrackingId());
            historyRequest.setEntityName("AGENT_TRAINING");
            historyRequest.setEntityTrackingId(savedHistory.getTrackingId());
            historyRequest.setActionType(ActionType.CREATE);
            historyRequest.setChangesSummary(
                    "Nouvelle formation: " + training.getTrainingName() + " du " + savedHistory.getStartDate() + " au " + savedHistory.getEndDate()
            );
            historyRequest.setNewValue(trainingSummary);

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

        return agentTrainingHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentTrainingHistoryResponse update(UUID trackingId, AgentTrainingHistoryRequest request) {
        AgentTrainingHistory history = agentTrainingHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent training history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        Training newTraining = trainingRepository.findByTrackingId(request.getTrainingTrackingId())
                .orElseThrow(() -> new RuntimeException("Training not found with trackingId: " + request.getTrainingTrackingId()));

        // Save old values for change detection
        String oldTrainingName = history.getTraining().getTrainingName();
        LocalDate oldStartDate = history.getStartDate();
        LocalDate oldEndDate = history.getEndDate();

        agentTrainingHistoryMapper.updateEntity(history, request, agent, newTraining);
        AgentTrainingHistory updatedHistory = agentTrainingHistoryRepository.save(history);

        // Detect and record changes in Histories
        try {
            StringBuilder changes = new StringBuilder();
            boolean hasChanges = false;

            if (!Objects.equals(oldTrainingName, newTraining.getTrainingName())) {
                changes.append("Formation: '").append(oldTrainingName).append("' → '").append(newTraining.getTrainingName()).append("' | ");
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
                historyRequest.setEntityName("AGENT_TRAINING");
                historyRequest.setEntityTrackingId(updatedHistory.getTrackingId());
                historyRequest.setActionType(ActionType.UPDATE);
                historyRequest.setChangesSummary(changesSummary);

                historiesService.create(historyRequest);
            }
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

        return agentTrainingHistoryMapper.toResponse(updatedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentTrainingHistoryResponse findByTrackingId(UUID trackingId) {
        AgentTrainingHistory history = agentTrainingHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent training history not found with trackingId: " + trackingId));
        return agentTrainingHistoryMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findByAgentTrackingId(UUID agentTrackingId) {
        return agentTrainingHistoryRepository.findByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findByTrainingTrackingId(UUID trainingTrackingId) {
        return agentTrainingHistoryRepository.findByTrainingTrackingId(trainingTrackingId)
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return agentTrainingHistoryRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findOngoingTrainingsByAgentTrackingId(UUID agentTrackingId) {
        return agentTrainingHistoryRepository.findOngoingTrainingsByAgentTrackingId(agentTrackingId)
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingHistoryResponse> findAll() {
        return agentTrainingHistoryRepository.findAll()
                .stream()
                .map(agentTrainingHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        AgentTrainingHistory history = agentTrainingHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent training history not found with trackingId: " + trackingId));

        // Save information for history record before deletion
        UUID agentTrackingId = history.getAgent().getTrackingId();
        String trainingName = history.getTraining().getTrainingName();
        LocalDate startDate = history.getStartDate();
        LocalDate endDate = history.getEndDate();

        agentTrainingHistoryRepository.delete(history);

        // Record deletion in Histories
        try {
            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agentTrackingId);
            historyRequest.setEntityName("AGENT_TRAINING");
            historyRequest.setEntityTrackingId(trackingId);
            historyRequest.setActionType(ActionType.DELETE);
            historyRequest.setChangesSummary(
                    "Suppression de la formation: " + trainingName + " du " + startDate + " au " + endDate
            );

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }
    }
}
