package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ConservatorSeizureRequest;
import com.helpysoft.mima_api.dto.ConservatorSeizureResponse;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.ConservatorSeizure;
import com.helpysoft.mima_api.mapper.ConservatorSeizureMapper;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.ConservatorSeizureRepository;
import com.helpysoft.mima_api.service.ConservatorSeizureService;
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
public class ConservatorSeizureServiceImpl implements ConservatorSeizureService {

    private final ConservatorSeizureRepository conservatorSeizureRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final ConservatorSeizureMapper conservatorSeizureMapper;

    @Override
    public ConservatorSeizureResponse create(ConservatorSeizureRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        ConservatorSeizure seizure = conservatorSeizureMapper.toEntity(request, ship);
        ConservatorSeizure saved = conservatorSeizureRepository.save(seizure);
        return conservatorSeizureMapper.toResponse(saved);
    }

    @Override
    public ConservatorSeizureResponse update(UUID trackingId, ConservatorSeizureRequest request) {
        ConservatorSeizure seizure = conservatorSeizureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Conservator seizure not found"));

        CommercialShips ship = commercialShipRepository.findByTrackingId(UUID.fromString(request.getCommercialShipTrackingId()))
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        conservatorSeizureMapper.updateEntity(seizure, request, ship);
        ConservatorSeizure updated = conservatorSeizureRepository.save(seizure);
        return conservatorSeizureMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ConservatorSeizureResponse findByTrackingId(UUID trackingId) {
        ConservatorSeizure seizure = conservatorSeizureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Conservator seizure not found"));
        return conservatorSeizureMapper.toResponse(seizure);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConservatorSeizureResponse> findByCommercialShip(UUID shipTrackingId) {
        return conservatorSeizureRepository.findByCommercialShipTrackingId(shipTrackingId)
                .stream()
                .map(conservatorSeizureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConservatorSeizureResponse> findActiveSeizures() {
        return conservatorSeizureRepository.findActiveSeizures()
                .stream()
                .map(conservatorSeizureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConservatorSeizureResponse> findByStatus(String status) {
        return conservatorSeizureRepository.findByStatus(status)
                .stream()
                .map(conservatorSeizureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConservatorSeizureResponse> findBySeizureDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return conservatorSeizureRepository.findBySeizureDateBetween(startDate, endDate)
                .stream()
                .map(conservatorSeizureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConservatorSeizureResponse> findBySeizingAuthority(String seizingAuthority) {
        return conservatorSeizureRepository.findBySeizingAuthority(seizingAuthority)
                .stream()
                .map(conservatorSeizureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConservatorSeizureResponse> findBySeizureType(String seizureType) {
        return conservatorSeizureRepository.findBySeizureType(seizureType)
                .stream()
                .map(conservatorSeizureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConservatorSeizureResponse> findAll() {
        return conservatorSeizureRepository.findAll()
                .stream()
                .map(conservatorSeizureMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ConservatorSeizure seizure = conservatorSeizureRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Conservator seizure not found"));
        conservatorSeizureRepository.delete(seizure);
    }
}
