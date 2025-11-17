package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.MissionParticipationsRequest;
import com.helpysoft.mima_api.dto.MissionParticipationsResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.MissionParticipations;
import com.helpysoft.mima_api.entity.Missions;
import com.helpysoft.mima_api.mapper.MissionParticipationsMapper;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.MissionParticipationsRepository;
import com.helpysoft.mima_api.repository.MissionsRepository;
import com.helpysoft.mima_api.service.MissionParticipationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionParticipationsServiceImpl implements MissionParticipationsService {

    private final MissionParticipationsRepository missionParticipationsRepository;
    private final MissionsRepository missionsRepository;
    private final AgentsRepository agentsRepository;
    private final MissionParticipationsMapper missionParticipationsMapper;

    @Override
    public MissionParticipationsResponse create(MissionParticipationsRequest request) {
        Missions mission = missionsRepository.findByTrackingId(request.getMissionTrackingId())
                .orElseThrow(() -> new RuntimeException("Mission not found with trackingId: " + request.getMissionTrackingId()));

        Agents agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));

        MissionParticipations participation = missionParticipationsMapper.toEntity(request, mission, agent);
        MissionParticipations savedParticipation = missionParticipationsRepository.save(participation);
        return missionParticipationsMapper.toResponse(savedParticipation);
    }

    @Override
    public MissionParticipationsResponse update(UUID trackingId, MissionParticipationsRequest request) {
        MissionParticipations participation = missionParticipationsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Mission participation not found with trackingId: " + trackingId));

        Missions mission = missionsRepository.findByTrackingId(request.getMissionTrackingId())
                .orElseThrow(() -> new RuntimeException("Mission not found with trackingId: " + request.getMissionTrackingId()));

        Agents agent = agentsRepository.findByTrackingId(request.getAgentTrackingId())
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + request.getAgentTrackingId()));

        participation.setMissionRule(request.getMissionRule());
        participation.setHoursCompleted(request.getHoursCompleted());
        participation.setMission(mission);
        participation.setAgent(agent);

        MissionParticipations updatedParticipation = missionParticipationsRepository.save(participation);
        return missionParticipationsMapper.toResponse(updatedParticipation);
    }

    @Override
    @Transactional(readOnly = true)
    public MissionParticipationsResponse findByTrackingId(UUID trackingId) {
        MissionParticipations participation = missionParticipationsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Mission participation not found with trackingId: " + trackingId));
        return missionParticipationsMapper.toResponse(participation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionParticipationsResponse> findByMission(UUID missionTrackingId) {
        Missions mission = missionsRepository.findByTrackingId(missionTrackingId)
                .orElseThrow(() -> new RuntimeException("Mission not found with trackingId: " + missionTrackingId));
        return missionParticipationsRepository.findByMission(mission)
                .stream()
                .map(missionParticipationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionParticipationsResponse> findByAgent(UUID agentTrackingId) {
        Agents agent = agentsRepository.findByTrackingId(agentTrackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + agentTrackingId));
        return missionParticipationsRepository.findByAgent(agent)
                .stream()
                .map(missionParticipationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionParticipationsResponse> findAll() {
        return missionParticipationsRepository.findAll()
                .stream()
                .map(missionParticipationsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        MissionParticipations participation = missionParticipationsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Mission participation not found with trackingId: " + trackingId));
        missionParticipationsRepository.delete(participation);
    }
}
