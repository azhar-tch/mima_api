package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentGradeHistoryRequest;
import com.helpysoft.mima_api.dto.AgentGradeHistoryResponse;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.mapper.AgentGradeHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentGradeHistory;
import com.helpysoft.mima_api.entity.HRGrade;
import com.helpysoft.mima_api.repository.AgentGradeHistoryRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.HRGradeRepository;
import com.helpysoft.mima_api.service.AgentGradeHistoryService;
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
public class AgentGradeHistoryServiceImpl implements AgentGradeHistoryService {
    private final AgentGradeHistoryRepository agentGradeHistoryRepository;
    private final AgentsRepository agentRepository;
    private final HRGradeRepository hrGradeRepository;
    private final AgentGradeHistoryMapper agentGradeHistoryMapper;
    private final HistoriesService historiesService;

    @Override
    public AgentGradeHistoryResponse create(AgentGradeHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        HRGrade grade = hrGradeRepository.findByTrackingId(request.getGradeTrackingId())
                .orElseThrow(() -> new RuntimeException("Grade not found with trackingId: " + request.getGradeTrackingId()));

        AgentGradeHistory history = agentGradeHistoryMapper.toEntity(request, agent, grade);
        AgentGradeHistory savedHistory = agentGradeHistoryRepository.save(history);

        // Record in Histories audit table
        try {
            String gradeSummary = String.format(
                    "{\"trackingId\":\"%s\",\"gradeName\":\"%s\",\"promotionDate\":\"%s\"}",
                    savedHistory.getTrackingId(),
                    grade.getGradeName(),
                    savedHistory.getPromotionDate()
            );

            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agent.getTrackingId());
            historyRequest.setEntityName("AGENT_GRADE");
            historyRequest.setEntityTrackingId(savedHistory.getTrackingId());
            historyRequest.setActionType(ActionType.CREATE);
            historyRequest.setChangesSummary(
                    "Nouveau grade attribué: " + grade.getGradeName() + " le " + savedHistory.getPromotionDate()
            );
            historyRequest.setNewValue(gradeSummary);

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

        return agentGradeHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentGradeHistoryResponse update(UUID trackingId, AgentGradeHistoryRequest request) {
        AgentGradeHistory history = agentGradeHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent grade history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        HRGrade newGrade = hrGradeRepository.findByTrackingId(request.getGradeTrackingId())
                .orElseThrow(() -> new RuntimeException("Grade not found with trackingId: " + request.getGradeTrackingId()));

        // Save old values for change detection
        String oldGradeName = history.getGrade().getGradeName();
        LocalDate oldPromotionDate = history.getPromotionDate();

        agentGradeHistoryMapper.updateEntity(history, request, agent, newGrade);
        AgentGradeHistory updatedHistory = agentGradeHistoryRepository.save(history);

        // Detect and record changes in Histories
        try {
            StringBuilder changes = new StringBuilder();
            boolean hasChanges = false;

            if (!Objects.equals(oldGradeName, newGrade.getGradeName())) {
                changes.append("Grade: '").append(oldGradeName).append("' → '").append(newGrade.getGradeName()).append("' | ");
                hasChanges = true;
            }

            if (!Objects.equals(oldPromotionDate, request.getPromotionDate())) {
                changes.append("Date de promotion: '").append(oldPromotionDate).append("' → '").append(request.getPromotionDate()).append("' | ");
                hasChanges = true;
            }

            if (hasChanges) {
                String changesSummary = changes.substring(0, changes.length() - 3);

                HistoriesRequest historyRequest = new HistoriesRequest();
                historyRequest.setAgentTrackingId(agent.getTrackingId());
                historyRequest.setEntityName("AGENT_GRADE");
                historyRequest.setEntityTrackingId(updatedHistory.getTrackingId());
                historyRequest.setActionType(ActionType.UPDATE);
                historyRequest.setChangesSummary(changesSummary);

                historiesService.create(historyRequest);
            }
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

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

        // Save information for history record before deletion
        UUID agentTrackingId = history.getAgent().getTrackingId();
        String gradeName = history.getGrade().getGradeName();
        LocalDate promotionDate = history.getPromotionDate();

        agentGradeHistoryRepository.delete(history);

        // Record deletion in Histories
        try {
            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agentTrackingId);
            historyRequest.setEntityName("AGENT_GRADE");
            historyRequest.setEntityTrackingId(trackingId);
            historyRequest.setActionType(ActionType.DELETE);
            historyRequest.setChangesSummary(
                    "Suppression du grade: " + gradeName + " du " + promotionDate
            );

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }
    }
}
