package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.PALEntryExitRequest;
import com.helpysoft.mima_api.dto.PALEntryExitResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.PALEntryExit;
import com.helpysoft.mima_api.mapper.PALEntryExitMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.PALEntryExitRepository;
import com.helpysoft.mima_api.service.PALEntryExitService;
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
public class PALEntryExitServiceImpl implements PALEntryExitService {

    private final PALEntryExitRepository palEntryExitRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final PALEntryExitMapper palEntryExitMapper;

    @Override
    public PALEntryExitResponse create(PALEntryExitRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        PALEntryExit entryExit = palEntryExitMapper.toEntity(request, ship);
        PALEntryExit saved = palEntryExitRepository.save(entryExit);
        return palEntryExitMapper.toResponse(saved);
    }

    @Override
    public PALEntryExitResponse update(UUID trackingId, PALEntryExitRequest request) {
        PALEntryExit entryExit = palEntryExitRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("PAL entry/exit record not found"));

        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        palEntryExitMapper.updateEntity(entryExit, request, ship);
        PALEntryExit updated = palEntryExitRepository.save(entryExit);
        return palEntryExitMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PALEntryExitResponse findByTrackingId(UUID trackingId) {
        PALEntryExit entryExit = palEntryExitRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("PAL entry/exit record not found"));
        return palEntryExitMapper.toResponse(entryExit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PALEntryExitResponse> findByCommercialShip(UUID shipTrackingId) {
        return palEntryExitRepository.findByCommercialShipTrackingId(shipTrackingId)
                .stream()
                .map(palEntryExitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PALEntryExitResponse> findShipsCurrentlyInPAL() {
        return palEntryExitRepository.findShipsCurrentlyInPAL()
                .stream()
                .map(palEntryExitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PALEntryExitResponse> findByEntryDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return palEntryExitRepository.findByEntryDateBetween(startDate, endDate)
                .stream()
                .map(palEntryExitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PALEntryExitResponse> findByExitDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return palEntryExitRepository.findByExitDateBetween(startDate, endDate)
                .stream()
                .map(palEntryExitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PALEntryExitResponse> findByAnchorageZone(String anchorageZone) {
        return palEntryExitRepository.findByAnchorageZone(anchorageZone)
                .stream()
                .map(palEntryExitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PALEntryExitResponse> findByBerthNumber(String berthNumber) {
        return palEntryExitRepository.findByBerthNumber(berthNumber)
                .stream()
                .map(palEntryExitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PALEntryExitResponse> findAll() {
        return palEntryExitRepository.findAll()
                .stream()
                .map(palEntryExitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PALEntryExitResponse> searchPALEntryExits(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return palEntryExitRepository.searchPALEntryExits(searchTerm)
                .stream()
                .map(palEntryExitMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        PALEntryExit entryExit = palEntryExitRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("PAL entry/exit record not found"));
        palEntryExitRepository.delete(entryExit);
    }
}
