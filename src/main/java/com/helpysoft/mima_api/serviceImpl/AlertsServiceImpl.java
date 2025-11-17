package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AlertsRequest;
import com.helpysoft.mima_api.dto.AlertsResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Alerts;
import com.helpysoft.mima_api.entity.AlertStatus;
import com.helpysoft.mima_api.mapper.AlertsMapper;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.AlertsRepository;
import com.helpysoft.mima_api.service.AlertsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertsServiceImpl implements AlertsService {

    private final AlertsRepository alertsRepository;
    private final AgentsRepository agentsRepository;
    private final AlertsMapper alertsMapper;

    @Override
    public AlertsResponse create(AlertsRequest request) {
        Agents agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));

        Alerts alert = alertsMapper.toEntity(request, agent);
        Alerts savedAlert = alertsRepository.save(alert);
        return alertsMapper.toResponse(savedAlert);
    }

    @Override
    public AlertsResponse update(UUID trackingId, AlertsRequest request) {
        Alerts alert = alertsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Alert not found with trackingId: " + trackingId));

        Agents agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));

        alert.setAlertType(request.getAlertType());
        alert.setDescription(request.getDescription());
        alert.setLevel(request.getLevel());
        alert.setStatus(request.getStatus());
        alert.setAgent(agent);

        Alerts updatedAlert = alertsRepository.save(alert);
        return alertsMapper.toResponse(updatedAlert);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertsResponse findByTrackingId(UUID trackingId) {
        Alerts alert = alertsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Alert not found with trackingId: " + trackingId));
        return alertsMapper.toResponse(alert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertsResponse> findByAgent(UUID agentTrackingId) {
        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + agentTrackingId));
        return alertsRepository.findByAgent(agent)
                .stream()
                .map(alertsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertsResponse> findByStatus(AlertStatus status) {
        return alertsRepository.findByStatus(status)
                .stream()
                .map(alertsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertsResponse> findAll() {
        return alertsRepository.findAll()
                .stream()
                .map(alertsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Alerts alert = alertsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Alert not found with trackingId: " + trackingId));
        alertsRepository.delete(alert);
    }
}
