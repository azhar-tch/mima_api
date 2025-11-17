package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ServicePositionRequest;
import com.helpysoft.mima_api.dto.ServicePositionResponse;
import com.helpysoft.mima_api.mapper.ServicePositionMapper;
import com.helpysoft.mima_api.entity.ServicePosition;
import com.helpysoft.mima_api.repository.ServicePositionRepository;
import com.helpysoft.mima_api.service.ServicePositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServicePositionServiceImpl implements ServicePositionService {
    private final ServicePositionRepository servicePositionRepository;
    private final ServicePositionMapper servicePositionMapper;

    @Override
    public ServicePositionResponse create(ServicePositionRequest request) {
        if (servicePositionRepository.existsByPositionName(request.getPositionName())) {
            throw new RuntimeException("Service position with name '" + request.getPositionName() + "' already exists");
        }
        ServicePosition position = servicePositionMapper.toEntity(request);
        ServicePosition savedPosition = servicePositionRepository.save(position);
        return servicePositionMapper.toResponse(savedPosition);
    }

    @Override
    public ServicePositionResponse update(UUID trackingId, ServicePositionRequest request) {
        ServicePosition position = servicePositionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Service position not found with trackingId: " + trackingId));
        servicePositionMapper.updateEntity(position, request);
        ServicePosition updatedPosition = servicePositionRepository.save(position);
        return servicePositionMapper.toResponse(updatedPosition);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicePositionResponse findByTrackingId(UUID trackingId) {
        ServicePosition position = servicePositionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Service position not found with trackingId: " + trackingId));
        return servicePositionMapper.toResponse(position);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicePositionResponse findByPositionName(String positionName) {
        ServicePosition position = servicePositionRepository.findByPositionName(positionName)
                .orElseThrow(() -> new RuntimeException("Service position not found with name: " + positionName));
        return servicePositionMapper.toResponse(position);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicePositionResponse> findByPositionType(String positionType) {
        return servicePositionRepository.findByPositionType(positionType)
                .stream()
                .map(servicePositionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicePositionResponse> findByLocation(String location) {
        return servicePositionRepository.findByLocation(location)
                .stream()
                .map(servicePositionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicePositionResponse> findAll() {
        return servicePositionRepository.findAll()
                .stream()
                .map(servicePositionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ServicePosition position = servicePositionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Service position not found with trackingId: " + trackingId));
        servicePositionRepository.delete(position);
    }
}
