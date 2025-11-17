package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.ArmedGuardMissionRequest;
import com.helpysoft.mima_api.dto.ArmedGuardMissionResponse;
import com.helpysoft.mima_api.entity.ArmedGuardMissions;
import com.helpysoft.mima_api.entity.CommercialShips;
import com.helpysoft.mima_api.entity.MissionStatus;
import com.helpysoft.mima_api.entity.SecurityAgencies;
import com.helpysoft.mima_api.mapper.ArmedGuardMissionMapper;
import com.helpysoft.mima_api.repository.ArmedGuardMissionRepository;
import com.helpysoft.mima_api.repository.CommercialShipRepository;
import com.helpysoft.mima_api.repository.SecurityAgencyRepository;
import com.helpysoft.mima_api.service.ArmedGuardMissionService;
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
public class ArmedGuardMissionServiceImpl implements ArmedGuardMissionService {

    private final ArmedGuardMissionRepository armedGuardMissionRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final SecurityAgencyRepository securityAgencyRepository;
    private final ArmedGuardMissionMapper armedGuardMissionMapper;

    @Override
    public ArmedGuardMissionResponse create(ArmedGuardMissionRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(request.getCommercialShipTrackingId())
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(request.getSecurityAgencyTrackingId())
                .orElseThrow(() -> new RuntimeException("Security agency not found"));

        ArmedGuardMissions mission = armedGuardMissionMapper.toEntity(request, ship, agency);
        mission.calculateDaysCount();
        ArmedGuardMissions savedMission = armedGuardMissionRepository.save(mission);
        return armedGuardMissionMapper.toResponse(savedMission);
    }

    @Override
    public ArmedGuardMissionResponse update(UUID trackingId, ArmedGuardMissionRequest request) {
        ArmedGuardMissions mission = armedGuardMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Armed guard mission not found"));

        CommercialShips ship = commercialShipRepository.findByTrackingId(request.getCommercialShipTrackingId())
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(request.getSecurityAgencyTrackingId())
                .orElseThrow(() -> new RuntimeException("Security agency not found"));

        mission.setCommercialShip(ship);
        mission.setSecurityAgency(agency);
        mission.setEmbarkationDate(request.getEmbarkationDate());
        mission.setDisembarkationDate(request.getDisembarkationDate());
        mission.setEmbarkationPort(request.getEmbarkationPort());
        mission.setDisembarkationPort(request.getDisembarkationPort());
        mission.setPersonnelCount(request.getPersonnelCount());
        mission.setPatrolZone(request.getPatrolZone());
        mission.setStatus(request.getStatus());
        mission.setIncidents(request.getIncidents());
        mission.setObservations(request.getObservations());
        mission.calculateDaysCount();

        ArmedGuardMissions updatedMission = armedGuardMissionRepository.save(mission);
        return armedGuardMissionMapper.toResponse(updatedMission);
    }

    @Override
    @Transactional(readOnly = true)
    public ArmedGuardMissionResponse findByTrackingId(UUID trackingId) {
        ArmedGuardMissions mission = armedGuardMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Armed guard mission not found"));
        return armedGuardMissionMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public ArmedGuardMissionResponse findByMissionNumber(String missionNumber) {
        ArmedGuardMissions mission = armedGuardMissionRepository.findByMissionNumber(missionNumber)
                .orElseThrow(() -> new RuntimeException("Armed guard mission not found"));
        return armedGuardMissionMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findByStatus(MissionStatus status) {
        return armedGuardMissionRepository.findByStatus(status)
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return armedGuardMissionRepository.findByPeriod(startDate, endDate)
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findByCommercialShip(UUID shipTrackingId) {
        CommercialShip ship = commercialShipRepository.findByTrackingId(shipTrackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));
        return armedGuardMissionRepository.findByCommercialShipId(ship.getId())
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findBySecurityAgency(UUID agencyTrackingId) {
        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(agencyTrackingId)
                .orElseThrow(() -> new RuntimeException("Security agency not found"));
        return armedGuardMissionRepository.findBySecurityAgencyId(agency.getId())
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmedGuardMissionResponse> findAll() {
        return armedGuardMissionRepository.findAll()
                .stream()
                .map(armedGuardMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        ArmedGuardMissions mission = armedGuardMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Armed guard mission not found"));
        armedGuardMissionRepository.delete(mission);
    }
}
