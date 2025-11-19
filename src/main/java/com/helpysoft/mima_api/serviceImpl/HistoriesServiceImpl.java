package com.helpysoft.mima_api.serviceImpl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.helpysoft.mima_api.dto.HistoriesRequest;
import com.helpysoft.mima_api.dto.HistoriesResponse;
import com.helpysoft.mima_api.entity.ActionType;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Histories;
import com.helpysoft.mima_api.mapper.HistoriesMapper;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.HistoriesRepository;
import com.helpysoft.mima_api.service.HistoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HistoriesServiceImpl implements HistoriesService {

    private final HistoriesRepository historiesRepository;
    private final AgentsRepository agentsRepository;
    private final HistoriesMapper historiesMapper;
    private final ObjectMapper objectMapper;

    @Override
    public HistoriesResponse create(HistoriesRequest request) {
        // Allow null agent for system-level actions or when no agent is associated
        Agents agent = null;
        if (request.getAgentTrackingId() != null) {
            agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                    .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));
        }

        Histories history = historiesMapper.toEntity(request, agent);
        Histories savedHistory = historiesRepository.save(history);
        return historiesMapper.toResponse(savedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public HistoriesResponse findByTrackingId(UUID trackingId) {
        Histories history = historiesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("History not found with trackingId: " + trackingId));
        return historiesMapper.toResponse(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriesResponse> findByAgent(UUID agentTrackingId) {
        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + agentTrackingId));
        return historiesRepository.findByAgent(agent)
                .stream()
                .map(historiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriesResponse> findByEntityTrackingId(UUID entityTrackingId) {
        return historiesRepository.findByEntityTrackingId(entityTrackingId)
                .stream()
                .map(historiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriesResponse> findByActionType(ActionType actionType) {
        return historiesRepository.findByActionType(actionType)
                .stream()
                .map(historiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriesResponse> findByEntityName(String entityName) {
        return historiesRepository.findByEntityName(entityName)
                .stream()
                .map(historiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriesResponse> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return historiesRepository.findByCreateDateBetween(startDate, endDate)
                .stream()
                .map(historiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoriesResponse> findAll() {
        return historiesRepository.findAll()
                .stream()
                .map(historiesMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Méthode utilitaire pour enregistrer l'historique avec sérialisation JSON
     *
     * @param agentTrackingId UUID de l'agent qui effectue l'action
     * @param entityName Nom de l'entité (DUTY, MISSION, ABSENCE, REST)
     * @param entityTrackingId UUID de l'entité concernée
     * @param actionType Type d'action (CREATE, UPDATE, DELETE)
     * @param changesSummary Résumé lisible des changements
     * @param oldEntity Entité avant modification (null pour CREATE)
     * @param newEntity Entité après modification (null pour DELETE)
     */
    public void recordHistory(
            UUID agentTrackingId,
            String entityName,
            UUID entityTrackingId,
            ActionType actionType,
            String changesSummary,
            Object oldEntity,
            Object newEntity
    ) {
        try {
            HistoriesRequest request = new HistoriesRequest();
            request.setAgentTrackingId(agentTrackingId);
            request.setEntityName(entityName);
            request.setEntityTrackingId(entityTrackingId);
            request.setActionType(actionType);
            request.setChangesSummary(changesSummary);

            // Sérialiser les entités en JSON (en ignorant les valeurs null)
            ObjectMapper historyMapper = objectMapper.copy();
            historyMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            if (oldEntity != null) {
                request.setOldValue(historyMapper.writeValueAsString(oldEntity));
            }
            if (newEntity != null) {
                request.setNewValue(historyMapper.writeValueAsString(newEntity));
            }

            create(request);
            log.info("✅ Historique enregistré: {} - {} - {}", entityName, actionType, changesSummary);
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'enregistrement de l'historique: {}", e.getMessage(), e);
        }
    }
}
