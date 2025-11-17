package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ManagementRulesRequest;
import com.helpysoft.mima_api.dto.ManagementRulesResponse;
import com.helpysoft.mima_api.entity.ManagementRules;
import com.helpysoft.mima_api.mapper.ManagementRulesMapper;
import com.helpysoft.mima_api.repository.ManagementRulesRepository;
import com.helpysoft.mima_api.service.ManagementRulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagementRulesServiceImpl implements ManagementRulesService {

    private final ManagementRulesRepository managementRulesRepository;
    private final ManagementRulesMapper managementRulesMapper;

    @Override
    public ManagementRulesResponse create(ManagementRulesRequest request) {
        ManagementRules managementRule = managementRulesMapper.toEntity(request);
        ManagementRules savedManagementRule = managementRulesRepository.save(managementRule);
        return managementRulesMapper.toResponse(savedManagementRule);
    }

    @Override
    public ManagementRulesResponse update(UUID trackingId, ManagementRulesRequest request) {
        // 1. Recherche de la règle existante
        ManagementRules managementRule = managementRulesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Management rule not found with trackingId: " + trackingId));

        // 2. Mise à jour via le mapper
        managementRulesMapper.updateEntity(managementRule, request);

        // 3. Sauvegarde en base
        ManagementRules updatedManagementRule = managementRulesRepository.save(managementRule);

        // 4. Retour en DTO response
        return managementRulesMapper.toResponse(updatedManagementRule);
    }

    @Override
    @Transactional(readOnly = true)
    public ManagementRulesResponse findByTrackingId(UUID trackingId) {
        ManagementRules managementRule = managementRulesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Management rule not found with trackingId: " + trackingId));
        return managementRulesMapper.toResponse(managementRule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagementRulesResponse> findAll() {
        return managementRulesRepository.findAll()
                .stream()
                .map(managementRulesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ManagementRules managementRule = managementRulesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Management rule not found with trackingId: " + trackingId));
        managementRulesRepository.delete(managementRule);
    }
}
