package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.EscortMissionRequest;
import com.helpysoft.mima_api.dto.EscortMissionResponse;
import com.helpysoft.mima_api.entity.*;
import com.helpysoft.mima_api.mapper.EscortMissionMapper;
import com.helpysoft.mima_api.repository.*;
import com.helpysoft.mima_api.service.EscortMissionService;
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
public class EscortMissionServiceImpl implements EscortMissionService {

    private final EscortMissionRepository escortMissionRepository;
    private final CommercialShipRepository commercialShipRepository;
    private final SecurityAgencyRepository securityAgencyRepository;
    private final NavalVesselRepository navalVesselRepository;
    private final AgentsRepository agentsRepository;
    private final EscortMissionMapper escortMissionMapper;

    @Override
    public EscortMissionResponse create(EscortMissionRequest request) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(request.getCommercialShipTrackingId())
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(request.getSecurityAgencyTrackingId())
                .orElseThrow(() -> new RuntimeException("Security agency not found"));

        NavalVessels vessel = navalVesselRepository.findByTrackingId(request.getNavalVesselTrackingId())
                .orElseThrow(() -> new RuntimeException("Naval vessel not found"));

        Agents commander = agentsRepository.findByTrackingId(request.getCommanderTrackingId())
                .orElseThrow(() -> new RuntimeException("Commander not found"));

        NavalVessels secondaryVessel = null;
        if (request.getSecondaryVesselTrackingId() != null) {
            secondaryVessel = navalVesselRepository.findByTrackingId(request.getSecondaryVesselTrackingId())
                    .orElse(null);
        }

        EscortMissions mission = escortMissionMapper.toEntity(request, ship, agency, vessel, commander, secondaryVessel);
        EscortMissions savedMission = escortMissionRepository.save(mission);
        return escortMissionMapper.toResponse(savedMission);
    }

    @Override
    public EscortMissionResponse update(UUID trackingId, EscortMissionRequest request) {
        EscortMissions mission = escortMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Escort mission not found"));

        CommercialShips ship = commercialShipRepository.findByTrackingId(request.getCommercialShipTrackingId())
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));

        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(request.getSecurityAgencyTrackingId())
                .orElseThrow(() -> new RuntimeException("Security agency not found"));

        NavalVessels vessel = navalVesselRepository.findByTrackingId(request.getNavalVesselTrackingId())
                .orElseThrow(() -> new RuntimeException("Naval vessel not found"));

        Agents commander = agentsRepository.findByTrackingId(request.getCommanderTrackingId())
                .orElseThrow(() -> new RuntimeException("Commander not found"));

        NavalVessels secondaryVessel = null;
        if (request.getSecondaryVesselTrackingId() != null) {
            secondaryVessel = navalVesselRepository.findByTrackingId(request.getSecondaryVesselTrackingId())
                    .orElse(null);
        }

        mission.setCommercialShip(ship);
        mission.setSecurityAgency(agency);
        mission.setNavalVessel(vessel);
        mission.setCommander(commander);
        mission.setCommanderRank(request.getCommanderRank());
        mission.setSecondaryVessel(secondaryVessel);
        mission.setVedettes(request.getVedettes());
        mission.setStartDate(request.getStartDate());
        mission.setEndDate(request.getEndDate());
        mission.setEscortType(request.getEscortType());
        mission.setDeparturePoint(request.getDeparturePoint());
        mission.setArrivalPoint(request.getArrivalPoint());
        mission.setDistance(request.getDistance());
        mission.setEscortZone(request.getEscortZone());
        mission.setStatus(request.getStatus());
        mission.setIncidents(request.getIncidents());
        mission.setObservations(request.getObservations());

        EscortMissions updatedMission = escortMissionRepository.save(mission);
        return escortMissionMapper.toResponse(updatedMission);
    }

    @Override
    @Transactional(readOnly = true)
    public EscortMissionResponse findByTrackingId(UUID trackingId) {
        EscortMissions mission = escortMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Escort mission not found"));
        return escortMissionMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public EscortMissionResponse findByMissionNumber(String missionNumber) {
        EscortMissions mission = escortMissionRepository.findByMissionNumber(missionNumber)
                .orElseThrow(() -> new RuntimeException("Escort mission not found"));
        return escortMissionMapper.toResponse(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByStatus(MissionStatus status) {
        return escortMissionRepository.findByStatus(status)
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return escortMissionRepository.findByPeriod(startDate, endDate)
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByCommercialShip(UUID shipTrackingId) {
        CommercialShips ship = commercialShipRepository.findByTrackingId(shipTrackingId)
                .orElseThrow(() -> new RuntimeException("Commercial ship not found"));
        return escortMissionRepository.findByCommercialShipId(ship.getId())
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findBySecurityAgency(UUID agencyTrackingId) {
        SecurityAgencies agency = securityAgencyRepository.findByTrackingId(agencyTrackingId)
                .orElseThrow(() -> new RuntimeException("Security agency not found"));
        return escortMissionRepository.findBySecurityAgencyId(agency.getId())
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByNavalVessel(UUID vesselTrackingId) {
        NavalVessels vessel = navalVesselRepository.findByTrackingId(vesselTrackingId)
                .orElseThrow(() -> new RuntimeException("Naval vessel not found"));
        return escortMissionRepository.findByNavalVesselId(vessel.getId())
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findByCommander(UUID commanderTrackingId) {
        Agents commander = agentsRepository.findByTrackingId(commanderTrackingId)
                .orElseThrow(() -> new RuntimeException("Commander not found"));
        return escortMissionRepository.findByCommanderId(commander.getId())
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EscortMissionResponse> findAll() {
        return escortMissionRepository.findAll()
                .stream()
                .map(escortMissionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        EscortMissions mission = escortMissionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Escort mission not found"));
        escortMissionRepository.delete(mission);
    }
}
