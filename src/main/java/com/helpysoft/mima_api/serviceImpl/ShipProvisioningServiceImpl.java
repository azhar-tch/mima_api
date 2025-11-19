package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ShipProvisioningRequest;
import com.helpysoft.mima_api.dto.ShipProvisioningResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ShipProvisioning;
import com.helpysoft.mima_api.mapper.ShipProvisioningMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.ShipProvisioningRepository;
import com.helpysoft.mima_api.service.ShipProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipProvisioningServiceImpl implements ShipProvisioningService {

    private final ShipProvisioningRepository shipProvisioningRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final ShipProvisioningMapper shipProvisioningMapper;

    @Override
    public ShipProvisioningResponse create(ShipProvisioningRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        ShipProvisioning provisioning = shipProvisioningMapper.toEntity(request, ship);
        ShipProvisioning saved = shipProvisioningRepository.save(provisioning);
        return shipProvisioningMapper.toResponse(saved);
    }

    @Override
    public ShipProvisioningResponse update(UUID trackingId, ShipProvisioningRequest request) {
        ShipProvisioning provisioning = shipProvisioningRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship provisioning not found"));

        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        shipProvisioningMapper.updateEntity(provisioning, request, ship);
        ShipProvisioning updated = shipProvisioningRepository.save(provisioning);
        return shipProvisioningMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipProvisioningResponse findByTrackingId(UUID trackingId) {
        ShipProvisioning provisioning = shipProvisioningRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship provisioning not found"));
        return shipProvisioningMapper.toResponse(provisioning);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipProvisioningResponse> findByCommercialShip(UUID shipTrackingId) {
        return shipProvisioningRepository.findByCommercialShipTrackingId(shipTrackingId)
                .stream()
                .map(shipProvisioningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipProvisioningResponse> findByProvisioningDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return shipProvisioningRepository.findByProvisioningDateBetween(startDate, endDate)
                .stream()
                .map(shipProvisioningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipProvisioningResponse> findByProvisioningType(String provisioningType) {
        return shipProvisioningRepository.findByProvisioningType(provisioningType)
                .stream()
                .map(shipProvisioningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipProvisioningResponse> findBySupplier(String supplierName) {
        return shipProvisioningRepository.findBySupplierName(supplierName)
                .stream()
                .map(shipProvisioningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipProvisioningResponse> findProvisioningsWithDelay() {
        return shipProvisioningRepository.findProvisioningsWithDelay()
                .stream()
                .map(shipProvisioningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipProvisioningResponse> findByProductType(String productType) {
        return shipProvisioningRepository.findByProductType(productType)
                .stream()
                .map(shipProvisioningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipProvisioningResponse> findAll() {
        return shipProvisioningRepository.findAll()
                .stream()
                .map(shipProvisioningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipProvisioningResponse> searchShipProvisionings(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return shipProvisioningRepository.searchShipProvisionings(searchTerm)
                .stream()
                .map(shipProvisioningMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ShipProvisioning provisioning = shipProvisioningRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship provisioning not found"));
        shipProvisioningRepository.delete(provisioning);
    }
}
