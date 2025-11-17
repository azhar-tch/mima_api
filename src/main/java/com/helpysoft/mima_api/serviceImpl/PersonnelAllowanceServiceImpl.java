package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.PersonnelAllowanceRequest;
import com.helpysoft.mima_api.dto.PersonnelAllowanceResponse;
import com.helpysoft.mima_api.entity.MaritimeRank;
import com.helpysoft.mima_api.entity.PersonnelAllowance;
import com.helpysoft.mima_api.mapper.PersonnelAllowanceMapper;
import com.helpysoft.mima_api.repository.PersonnelAllowanceRepository;
import com.helpysoft.mima_api.service.PersonnelAllowanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonnelAllowanceServiceImpl implements PersonnelAllowanceService {

    private final PersonnelAllowanceRepository personnelAllowanceRepository;
    private final PersonnelAllowanceMapper personnelAllowanceMapper;

    @Override
    public PersonnelAllowanceResponse create(PersonnelAllowanceRequest request) {
        PersonnelAllowance allowance = personnelAllowanceMapper.toEntity(request);
        PersonnelAllowance savedAllowance = personnelAllowanceRepository.save(allowance);
        return personnelAllowanceMapper.toResponse(savedAllowance);
    }

    @Override
    public PersonnelAllowanceResponse update(UUID trackingId, PersonnelAllowanceRequest request) {
        PersonnelAllowance allowance = personnelAllowanceRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Personnel allowance not found with trackingId: " + trackingId));

        allowance.setRankCode(request.getRankCode());
        allowance.setMaritimeRank(request.getMaritimeRank());
        allowance.setEscortDailyAllowance(request.getEscortDailyAllowance());
        allowance.setArmedGuardDailyAllowance(request.getArmedGuardDailyAllowance());
        allowance.setPatrolAllowance(request.getPatrolAllowance());
        allowance.setRiskAllowance(request.getRiskAllowance());
        allowance.setSeaAllowance(request.getSeaAllowance());
        allowance.setCurrency(request.getCurrency());
        allowance.setObservations(request.getObservations());
        allowance.setIsActive(request.getIsActive());

        PersonnelAllowance updatedAllowance = personnelAllowanceRepository.save(allowance);
        return personnelAllowanceMapper.toResponse(updatedAllowance);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonnelAllowanceResponse findByTrackingId(UUID trackingId) {
        PersonnelAllowance allowance = personnelAllowanceRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Personnel allowance not found with trackingId: " + trackingId));
        return personnelAllowanceMapper.toResponse(allowance);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonnelAllowanceResponse findByRankCode(String rankCode) {
        PersonnelAllowance allowance = personnelAllowanceRepository.findByRankCode(rankCode)
                .orElseThrow(() -> new RuntimeException("Personnel allowance not found with rank code: " + rankCode));
        return personnelAllowanceMapper.toResponse(allowance);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonnelAllowanceResponse findByMaritimeRank(MaritimeRank maritimeRank) {
        PersonnelAllowance allowance = personnelAllowanceRepository.findByMaritimeRank(maritimeRank)
                .orElseThrow(() -> new RuntimeException("Personnel allowance not found for maritime rank: " + maritimeRank));
        return personnelAllowanceMapper.toResponse(allowance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonnelAllowanceResponse> findActiveAllowances() {
        return personnelAllowanceRepository.findByIsActiveTrue()
                .stream()
                .map(personnelAllowanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonnelAllowanceResponse> findAllOrderByAllowance() {
        return personnelAllowanceRepository.findAllOrderByAllowance()
                .stream()
                .map(personnelAllowanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonnelAllowanceResponse> findAll() {
        return personnelAllowanceRepository.findAll()
                .stream()
                .map(personnelAllowanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        PersonnelAllowance allowance = personnelAllowanceRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Personnel allowance not found with trackingId: " + trackingId));
        personnelAllowanceRepository.delete(allowance);
    }
}
