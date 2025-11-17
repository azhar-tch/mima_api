package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.STSOperationRequest;
import com.helpysoft.mima_api.dto.STSOperationResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.STSOperation;
import com.helpysoft.mima_api.mapper.STSOperationMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.NavalVesselRepository;
import com.helpysoft.mima_api.repository.STSOperationRepository;
import com.helpysoft.mima_api.service.STSOperationService;
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
public class STSOperationServiceImpl implements STSOperationService {

    private final STSOperationRepository stsOperationRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final NavalVesselRepository navalVesselRepository;
    private final STSOperationMapper stsOperationMapper;

    @Override
    public STSOperationResponse create(STSOperationRequest request) {
        CommercialShips motherVessel = commercialShipRepository.findByTrackingId(UUID.fromString(request.getMotherVesselTrackingId()))
                .orElseThrow(() -> new RuntimeException("Mother vessel not found"));

        CommercialShips receivingVessel = commercialShipRepository.findByTrackingId(UUID.fromString(request.getReceivingVesselTrackingId()))
                .orElseThrow(() -> new RuntimeException("Receiving vessel not found"));

        NavalVessels supervisingVessel = null;
        if (request.getSupervisingNavalVesselTrackingId() != null) {
            supervisingVessel = navalVesselRepository.findByTrackingId(UUID.fromString(request.getSupervisingNavalVesselTrackingId()))
                    .orElse(null);
        }

        STSOperation stsOperation = stsOperationMapper.toEntity(request, motherVessel, receivingVessel, supervisingVessel);
        STSOperation saved = stsOperationRepository.save(stsOperation);
        return stsOperationMapper.toResponse(saved);
    }

    @Override
    public STSOperationResponse update(UUID trackingId, STSOperationRequest request) {
        STSOperation stsOperation = stsOperationRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("STS operation not found"));

        CommercialShips motherVessel = commercialShipRepository.findByTrackingId(UUID.fromString(request.getMotherVesselTrackingId()))
                .orElseThrow(() -> new RuntimeException("Mother vessel not found"));

        CommercialShips receivingVessel = commercialShipRepository.findByTrackingId(UUID.fromString(request.getReceivingVesselTrackingId()))
                .orElseThrow(() -> new RuntimeException("Receiving vessel not found"));

        NavalVessels supervisingVessel = null;
        if (request.getSupervisingNavalVesselTrackingId() != null) {
            supervisingVessel = navalVesselRepository.findByTrackingId(UUID.fromString(request.getSupervisingNavalVesselTrackingId()))
                    .orElse(null);
        }

        stsOperationMapper.updateEntity(stsOperation, request, motherVessel, receivingVessel, supervisingVessel);
        STSOperation updated = stsOperationRepository.save(stsOperation);
        return stsOperationMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public STSOperationResponse findByTrackingId(UUID trackingId) {
        STSOperation stsOperation = stsOperationRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("STS operation not found"));
        return stsOperationMapper.toResponse(stsOperation);
    }

    @Override
    @Transactional(readOnly = true)
    public STSOperationResponse findByOperationNumber(String operationNumber) {
        STSOperation stsOperation = stsOperationRepository.findByOperationNumber(operationNumber)
                .orElseThrow(() -> new RuntimeException("STS operation not found"));
        return stsOperationMapper.toResponse(stsOperation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findByMotherVessel(UUID vesselTrackingId) {
        CommercialShips vessel = commercialShipRepository.findByTrackingId(vesselTrackingId)
                .orElseThrow(() -> new RuntimeException("Mother vessel not found"));
        return stsOperationRepository.findByMotherVesselId(vessel.getId())
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findByReceivingVessel(UUID vesselTrackingId) {
        CommercialShips vessel = commercialShipRepository.findByTrackingId(vesselTrackingId)
                .orElseThrow(() -> new RuntimeException("Receiving vessel not found"));
        return stsOperationRepository.findByReceivingVesselId(vessel.getId())
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findBySupervisingVessel(UUID vesselTrackingId) {
        NavalVessels vessel = navalVesselRepository.findByTrackingId(vesselTrackingId)
                .orElseThrow(() -> new RuntimeException("Supervising naval vessel not found"));
        return stsOperationRepository.findBySupervisingNavalVesselId(vessel.getId())
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findOngoingOperations() {
        return stsOperationRepository.findOngoingOperations()
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findByStatus(String status) {
        return stsOperationRepository.findByStatus(status)
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return stsOperationRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findByCargoType(String cargoType) {
        return stsOperationRepository.findByCargoType(cargoType)
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findOperationsWithPollution() {
        return stsOperationRepository.findOperationsWithPollution()
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findByMaritimeZone(String maritimeZone) {
        return stsOperationRepository.findByMaritimeZone(maritimeZone)
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<STSOperationResponse> findAll() {
        return stsOperationRepository.findAll()
                .stream()
                .map(stsOperationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        STSOperation stsOperation = stsOperationRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("STS operation not found"));
        stsOperationRepository.delete(stsOperation);
    }
}
