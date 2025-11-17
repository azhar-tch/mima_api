package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.OtherPositionRequest;
import com.helpysoft.mima_api.dto.OtherPositionResponse;
import com.helpysoft.mima_api.mapper.OtherPositionMapper;
import com.helpysoft.mima_api.entity.OtherPosition;
import com.helpysoft.mima_api.repository.OtherPositionRepository;
import com.helpysoft.mima_api.service.OtherPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OtherPositionServiceImpl implements OtherPositionService {
    private final OtherPositionRepository otherPositionRepository;
    private final OtherPositionMapper otherPositionMapper;

    @Override
    public OtherPositionResponse create(OtherPositionRequest request) {
        if (otherPositionRepository.existsByPositionName(request.getPositionName())) {
            throw new RuntimeException("Other position with name '" + request.getPositionName() + "' already exists");
        }
        OtherPosition position = otherPositionMapper.toEntity(request);
        OtherPosition savedPosition = otherPositionRepository.save(position);
        return otherPositionMapper.toResponse(savedPosition);
    }

    @Override
    public OtherPositionResponse update(UUID trackingId, OtherPositionRequest request) {
        OtherPosition position = otherPositionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Other position not found with trackingId: " + trackingId));
        otherPositionMapper.updateEntity(position, request);
        OtherPosition updatedPosition = otherPositionRepository.save(position);
        return otherPositionMapper.toResponse(updatedPosition);
    }

    @Override
    @Transactional(readOnly = true)
    public OtherPositionResponse findByTrackingId(UUID trackingId) {
        OtherPosition position = otherPositionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Other position not found with trackingId: " + trackingId));
        return otherPositionMapper.toResponse(position);
    }

    @Override
    @Transactional(readOnly = true)
    public OtherPositionResponse findByPositionName(String positionName) {
        OtherPosition position = otherPositionRepository.findByPositionName(positionName)
                .orElseThrow(() -> new RuntimeException("Other position not found with name: " + positionName));
        return otherPositionMapper.toResponse(position);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OtherPositionResponse> findByPositionType(String positionType) {
        return otherPositionRepository.findByPositionType(positionType)
                .stream()
                .map(otherPositionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OtherPositionResponse> findAll() {
        return otherPositionRepository.findAll()
                .stream()
                .map(otherPositionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        OtherPosition position = otherPositionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Other position not found with trackingId: " + trackingId));
        otherPositionRepository.delete(position);
    }
}
