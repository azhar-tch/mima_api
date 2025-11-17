package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.NavalVesselRequest;
import com.helpysoft.mima_api.dto.NavalVesselResponse;
import com.helpysoft.mima_api.entity.NavalVessels;
import com.helpysoft.mima_api.entity.NavalVesselStatus;
import com.helpysoft.mima_api.entity.NavalVesselType;
import com.helpysoft.mima_api.mapper.NavalVesselMapper;
import com.helpysoft.mima_api.repository.NavalVesselRepository;
import com.helpysoft.mima_api.service.NavalVesselService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NavalVesselServiceImpl implements NavalVesselService {

    private final NavalVesselRepository navalVesselRepository;
    private final NavalVesselMapper navalVesselMapper;

    @Override
    public NavalVesselResponse create(NavalVesselRequest request) {
        NavalVessels vessel = navalVesselMapper.toEntity(request);
        NavalVessels savedVessel = navalVesselRepository.save(vessel);
        return navalVesselMapper.toResponse(savedVessel);
    }

    @Override
    public NavalVesselResponse update(UUID trackingId, NavalVesselRequest request) {
        NavalVessels vessel = navalVesselRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found with trackingId: " + trackingId));

        vessel.setVesselNumber(request.getVesselNumber());
        vessel.setVesselType(request.getVesselType());
        vessel.setVesselName(request.getVesselName());
        vessel.setHullNumber(request.getHullNumber());
        vessel.setYearCommissioned(request.getYearCommissioned());
        vessel.setDateCommissioned(request.getDateCommissioned());
        vessel.setDateDecommissioned(request.getDateDecommissioned());
        vessel.setLength(request.getLength());
        vessel.setWidth(request.getWidth());
        vessel.setDraft(request.getDraft());
        vessel.setDisplacement(request.getDisplacement());
        vessel.setMaxSpeed(request.getMaxSpeed());
        vessel.setCrewCapacity(request.getCrewCapacity());
        vessel.setFuelCapacity(request.getFuelCapacity());
        vessel.setRange(request.getRange());
        vessel.setArmament(request.getArmament());
        vessel.setElectronics(request.getElectronics());
        vessel.setEngineType(request.getEngineType());
        vessel.setEnginePower(request.getEnginePower());
        vessel.setHomePort(request.getHomePort());
        vessel.setOperationalStatus(request.getOperationalStatus());
        vessel.setCurrentLocation(request.getCurrentLocation());
        vessel.setCurrentMission(request.getCurrentMission());
        vessel.setLastMaintenanceDate(request.getLastMaintenanceDate());
        vessel.setNextMaintenanceDate(request.getNextMaintenanceDate());
        vessel.setTotalOperationalHours(request.getTotalOperationalHours());
        vessel.setObservations(request.getObservations());
        vessel.setIsActive(request.getIsActive());

        NavalVessels updatedVessel = navalVesselRepository.save(vessel);
        return navalVesselMapper.toResponse(updatedVessel);
    }

    @Override
    @Transactional(readOnly = true)
    public NavalVesselResponse findByTrackingId(UUID trackingId) {
        NavalVessels vessel = navalVesselRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found with trackingId: " + trackingId));
        return navalVesselMapper.toResponse(vessel);
    }

    @Override
    @Transactional(readOnly = true)
    public NavalVesselResponse findByVesselNumber(String vesselNumber) {
        NavalVessels vessel = navalVesselRepository.findByVesselNumber(vesselNumber)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found with vessel number: " + vesselNumber));
        return navalVesselMapper.toResponse(vessel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findByVesselType(NavalVesselType vesselType) {
        return navalVesselRepository.findByVesselType(vesselType)
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findByOperationalStatus(NavalVesselStatus status) {
        return navalVesselRepository.findByOperationalStatus(status)
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findAvailableVessels() {
        return navalVesselRepository.findAvailableVessels()
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findPatrolVessels() {
        return navalVesselRepository.findPatrolVessels()
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NavalVesselResponse> findAll() {
        return navalVesselRepository.findAll()
                .stream()
                .map(navalVesselMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        NavalVessels vessel = navalVesselRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found with trackingId: " + trackingId));
        navalVesselRepository.delete(vessel);
    }
}
