package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.UnitsRequest;
import com.helpysoft.mima_api.dto.UnitsResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Units;
import com.helpysoft.mima_api.entity.UnitType;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.UnitsMapper;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.UnitsRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.UnitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UnitsServiceImpl implements UnitsService {

    private final UnitsRepository unitsRepository;
    private final AgentsRepository agentsRepository;
    private final UnitsMapper unitsMapper;

    @Override
    public UnitsResponse create(UnitsRequest request) {
        Agents chief = null;
        if (request.getChiefTrackingId() != null) {
            chief = agentsRepository.findByTrackingId(request.getChiefTrackingId())
                    .orElseThrow(() -> new RuntimeException("Chief not found with trackingId: " + request.getChiefTrackingId()));
        }

        Units unit = unitsMapper.toEntity(request, chief);
        Units savedUnit = unitsRepository.save(unit);
        return unitsMapper.toResponse(savedUnit);
    }

    @Override
    public UnitsResponse update(UUID trackingId, UnitsRequest request) {
        Units unit = unitsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + trackingId));

        Agents chief = null;
        if (request.getChiefTrackingId() != null) {
            chief = agentsRepository.findByTrackingId(request.getChiefTrackingId())
                    .orElseThrow(() -> new RuntimeException("Chief not found with trackingId: " + request.getChiefTrackingId()));
        }

        unit.setName(request.getName());
        unit.setDescription(request.getDescription());
        unit.setType(request.getType());
        unit.setStatus(request.getStatus());
        unit.setChief(chief);

        Units updatedUnit = unitsRepository.save(unit);
        return unitsMapper.toResponse(updatedUnit);
    }

    @Override
    @Transactional(readOnly = true)
    public UnitsResponse findByTrackingId(UUID trackingId) {
        Units unit = unitsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + trackingId));
        return unitsMapper.toResponse(unit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitsResponse> findByType(UnitType type) {
        return unitsRepository.findByType(type)
                .stream()
                .map(unitsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitsResponse> findAll() {
        return unitsRepository.findAll()
                .stream()
                .map(unitsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Units unit = unitsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + trackingId));
        unitsRepository.delete(unit);
    }
}
