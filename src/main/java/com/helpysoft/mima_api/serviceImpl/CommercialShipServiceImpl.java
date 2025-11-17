package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.CommercialShipRequest;
import com.helpysoft.mima_api.dto.CommercialShipResponse;
import com.helpysoft.mima_api.entity.CommercialShip;
import com.helpysoft.mima_api.mapper.CommercialShipMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.service.CommercialShipService;
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
public class CommercialShipServiceImpl implements CommercialShipService {

    private final CommercialShipRepository commercialShipRepository;
    private final CommercialShipMapper commercialShipMapper;

    @Override
    public CommercialShipResponse create(CommercialShipRequest request) {
        CommercialShip ship = commercialShipMapper.toEntity(request);
        CommercialShip savedShip = commercialShipRepository.save(ship);
        return commercialShipMapper.toResponse(savedShip);
    }

    @Override
    public CommercialShipResponse update(UUID trackingId, CommercialShipRequest request) {
        CommercialShip ship = commercialShipRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found with trackingId: " + trackingId));

        ship.setImoNumber(request.getImoNumber());
        ship.setShipName(request.getShipName());
        ship.setShipType(request.getShipType());
        ship.setFlag(request.getFlag());
        ship.setMmsi(request.getMmsi());
        ship.setCallSign(request.getCallSign());
        ship.setGrossTonnage(request.getGrossTonnage());
        ship.setDeadWeight(request.getDeadWeight());
        ship.setLength(request.getLength());
        ship.setWidth(request.getWidth());
        ship.setDraft(request.getDraft());
        ship.setYearBuilt(request.getYearBuilt());
        ship.setShipOwner(request.getShipOwner());
        ship.setOperator(request.getOperator());
        ship.setLastPort(request.getLastPort());
        ship.setNextPort(request.getNextPort());
        ship.setCargoType(request.getCargoType());
        ship.setArrivalDate(request.getArrivalDate());
        ship.setDepartureDate(request.getDepartureDate());
        ship.setStatus(request.getStatus());
        ship.setObservations(request.getObservations());
        ship.setIsActive(request.getIsActive());

        CommercialShip updatedShip = commercialShipRepository.save(ship);
        return commercialShipMapper.toResponse(updatedShip);
    }

    @Override
    @Transactional(readOnly = true)
    public CommercialShipResponse findByTrackingId(UUID trackingId) {
        CommercialShip ship = commercialShipRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found with trackingId: " + trackingId));
        return commercialShipMapper.toResponse(ship);
    }

    @Override
    @Transactional(readOnly = true)
    public CommercialShipResponse findByImoNumber(String imoNumber) {
        CommercialShip ship = commercialShipRepository.findByImoNumber(imoNumber)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found with IMO number: " + imoNumber));
        return commercialShipMapper.toResponse(ship);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findByShipType(String shipType) {
        return commercialShipRepository.findByShipType(shipType)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findByStatus(String status) {
        return commercialShipRepository.findByStatus(status)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findByFlag(String flag) {
        return commercialShipRepository.findByFlag(flag)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return commercialShipRepository.findByArrivalDateBetween(startDate, endDate)
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommercialShipResponse> findAll() {
        return commercialShipRepository.findAll()
                .stream()
                .map(commercialShipMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        CommercialShip ship = commercialShipRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found with trackingId: " + trackingId));
        commercialShipRepository.delete(ship);
    }
}
