package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ShipArrivalDepartureRequest;
import com.helpysoft.mima_api.dto.ShipArrivalDepartureResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ShipArrivalDeparture;
import com.helpysoft.mima_api.mapper.ShipArrivalDepartureMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.ShipArrivalDepartureRepository;
import com.helpysoft.mima_api.service.ShipArrivalDepartureService;
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
public class ShipArrivalDepartureServiceImpl implements ShipArrivalDepartureService {

    private final ShipArrivalDepartureRepository arrivalDepartureRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final ShipArrivalDepartureMapper arrivalDepartureMapper;

    @Override
    public ShipArrivalDepartureResponse create(ShipArrivalDepartureRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        ShipArrivalDeparture arrivalDeparture = arrivalDepartureMapper.toEntity(request, ship);
        ShipArrivalDeparture saved = arrivalDepartureRepository.save(arrivalDeparture);
        return arrivalDepartureMapper.toResponse(saved);
    }

    @Override
    public ShipArrivalDepartureResponse update(UUID trackingId, ShipArrivalDepartureRequest request) {
        ShipArrivalDeparture arrivalDeparture = arrivalDepartureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship arrival/departure record not found"));

        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        arrivalDepartureMapper.updateEntity(arrivalDeparture, request, ship);
        ShipArrivalDeparture updated = arrivalDepartureRepository.save(arrivalDeparture);
        return arrivalDepartureMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipArrivalDepartureResponse findByTrackingId(UUID trackingId) {
        ShipArrivalDeparture arrivalDeparture = arrivalDepartureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship arrival/departure record not found"));
        return arrivalDepartureMapper.toResponse(arrivalDeparture);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByCommercialShip(UUID shipTrackingId) {
        return arrivalDepartureRepository.findByCommercialShipTrackingId(shipTrackingId)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findShipsCurrentlyInPort() {
        return arrivalDepartureRepository.findShipsCurrentlyInPort()
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByArrivalDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return arrivalDepartureRepository.findByArrivalDateBetween(startDate, endDate)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByDepartureDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return arrivalDepartureRepository.findByDepartureDateBetween(startDate, endDate)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByPortOfOrigin(String portOfOrigin) {
        return arrivalDepartureRepository.findByPortOfOrigin(portOfOrigin)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findByNextDestination(String nextDestination) {
        return arrivalDepartureRepository.findByPortOfDestination(nextDestination)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> findAll() {
        return arrivalDepartureRepository.findAll()
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShipArrivalDepartureResponse> searchShipArrivalDepartures(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return arrivalDepartureRepository.searchShipArrivalDepartures(searchTerm)
                .stream()
                .map(arrivalDepartureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ShipArrivalDeparture arrivalDeparture = arrivalDepartureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Ship arrival/departure record not found"));
        arrivalDepartureRepository.delete(arrivalDeparture);
    }
}
