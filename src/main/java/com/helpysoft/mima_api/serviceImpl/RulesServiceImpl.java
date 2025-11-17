package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.RulesRequest;
import com.helpysoft.mima_api.dto.RulesResponse;
import com.helpysoft.mima_api.entity.Rules;
import com.helpysoft.mima_api.mapper.RulesMapper;
import com.helpysoft.mima_api.repository.RulesRepository;
import com.helpysoft.mima_api.service.RulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RulesServiceImpl implements RulesService {

    private final RulesRepository rulesRepository;
    private final RulesMapper rulesMapper;

    @Override
    public RulesResponse create(RulesRequest request) {
        Rules rule = rulesMapper.toEntity(request);
        Rules savedRule = rulesRepository.save(rule);
        return rulesMapper.toResponse(savedRule);
    }

    @Override
    public RulesResponse update(UUID trackingId, RulesRequest request) {
        Rules rule = rulesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Rule not found with trackingId: " + trackingId));

        rule.setTitle(request.getTitle());
        rule.setDescription(request.getDescription());

        Rules updatedRule = rulesRepository.save(rule);
        return rulesMapper.toResponse(updatedRule);
    }

    @Override
    @Transactional(readOnly = true)
    public RulesResponse findByTrackingId(UUID trackingId) {
        Rules rule = rulesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Rule not found with trackingId: " + trackingId));
        return rulesMapper.toResponse(rule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RulesResponse> findAll() {
        return rulesRepository.findAll()
                .stream()
                .map(rulesMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Rules rule = rulesRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Rule not found with trackingId: " + trackingId));
        rulesRepository.delete(rule);
    }
}
