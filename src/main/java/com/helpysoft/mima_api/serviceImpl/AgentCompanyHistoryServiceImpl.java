package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentCompanyHistoryRequest;
import com.helpysoft.mima_api.dto.AgentCompanyHistoryResponse;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.mapper.AgentCompanyHistoryMapper;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentCompanyHistory;
import com.helpysoft.mima_api.entity.BMLCompany;
import com.helpysoft.mima_api.repository.AgentCompanyHistoryRepository;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.BMLCompanyRepository;
import com.helpysoft.mima_api.service.AgentCompanyHistoryService;
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
public class AgentCompanyHistoryServiceImpl implements AgentCompanyHistoryService {
    private final AgentCompanyHistoryRepository agentCompanyHistoryRepository;
    private final AgentsRepository agentRepository;
    private final BMLCompanyRepository bmlCompanyRepository;
    private final AgentCompanyHistoryMapper agentCompanyHistoryMapper;
    private final HistoriesService historiesService;

    @Override
    public AgentCompanyHistoryResponse create(AgentCompanyHistoryRequest request) {
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        BMLCompany company = bmlCompanyRepository.findByTrackingId(request.getCompanyTrackingId())
                .orElseThrow(() -> new RuntimeException("Company not found with trackingId: " + request.getCompanyTrackingId()));

        AgentCompanyHistory history = agentCompanyHistoryMapper.toEntity(request, agent, company);
        AgentCompanyHistory savedHistory = agentCompanyHistoryRepository.save(history);

        // Record in Histories audit table
        try {
            String companySummary = String.format(
                    "{\"trackingId\":\"%s\",\"companyName\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\"}",
                    savedHistory.getTrackingId(),
                    company.getCompanyName(),
                    savedHistory.getStartDate(),
                    savedHistory.getEndDate()
            );

            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agent.getTrackingId());
            historyRequest.setEntityName("AGENT_COMPANY");
            historyRequest.setEntityTrackingId(savedHistory.getTrackingId());
            historyRequest.setActionType(ActionType.CREATE);
            historyRequest.setChangesSummary(
                    "Affectation à la compagnie: " + company.getCompanyName() + " du " + savedHistory.getStartDate() +
                    (savedHistory.getEndDate() != null ? " au " + savedHistory.getEndDate() : "")
            );
            historyRequest.setNewValue(companySummary);

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

        return agentCompanyHistoryMapper.toResponse(savedHistory);
    }

    @Override
    public AgentCompanyHistoryResponse update(UUID trackingId, AgentCompanyHistoryRequest request) {
        AgentCompanyHistory history = agentCompanyHistoryRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent company history not found with trackingId: " + trackingId));
        Agents agent = agentRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        BMLCompany newCompany = bmlCompanyRepository.findByTrackingId(request.getCompanyTrackingId())
                .orElseThrow(() -> new RuntimeException("Company not found with trackingId: " + request.getCompanyTrackingId()));

        // Save old values for change detection
        String oldCompanyName = history.getCompany().getCompanyName();
        LocalDate oldStartDate = history.getStartDate();
        LocalDate oldEndDate = history.getEndDate();

        agentCompanyHistoryMapper.updateEntity(history, request, agent, newCompany);
        AgentCompanyHistory updatedHistory = agentCompanyHistoryRepository.save(history);

        // Detect and record changes in Histories
        try {
            StringBuilder changes = new StringBuilder();
            boolean hasChanges = false;

            if (!Objects.equals(oldCompanyName, newCompany.getCompanyName())) {
                changes.append("Compagnie: '").append(oldCompanyName).append("' → '").append(newCompany.getCompanyName()).append("' | ");
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
                historyRequest.setEntityName("AGENT_COMPANY");
                historyRequest.setEntityTrackingId(updatedHistory.getTrackingId());
                historyRequest.setActionType(ActionType.UPDATE);
                historyRequest.setChangesSummary(changesSummary);

                historiesService.create(historyRequest);
            }
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }

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

        // Save information for history record before deletion
        UUID agentTrackingId = history.getAgent().getTrackingId();
        String companyName = history.getCompany().getCompanyName();
        LocalDate startDate = history.getStartDate();
        LocalDate endDate = history.getEndDate();

        agentCompanyHistoryRepository.delete(history);

        // Record deletion in Histories
        try {
            HistoriesRequest historyRequest = new HistoriesRequest();
            historyRequest.setAgentTrackingId(agentTrackingId);
            historyRequest.setEntityName("AGENT_COMPANY");
            historyRequest.setEntityTrackingId(trackingId);
            historyRequest.setActionType(ActionType.DELETE);
            historyRequest.setChangesSummary(
                    "Suppression de l'affectation compagnie: " + companyName + " du " + startDate +
                    (endDate != null ? " au " + endDate : "")
            );

            historiesService.create(historyRequest);
        } catch (Exception e) {
            // Log error but don't fail the main operation
        }
    }
}
