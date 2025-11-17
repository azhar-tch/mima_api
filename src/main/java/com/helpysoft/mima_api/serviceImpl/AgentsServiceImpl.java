package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentsRequest;
import com.helpysoft.mima_api.dto.AgentsResponse;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.AgentStatus;
import com.helpysoft.mima_api.entity.Units;
import com.helpysoft.mima_api.mapper.AgentsMapper;
import com.helpysoft.mima_api.repository.AgentsRepository;
import com.helpysoft.mima_api.repository.UnitsRepository;
import com.helpysoft.mima_api.service.AgentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentsServiceImpl implements AgentsService {

    private final AgentsRepository agentsRepository;
    private final UnitsRepository unitsRepository;
    private final AgentsMapper agentsMapper;

    @Override
    public AgentsResponse create(AgentsRequest request) {
        Units unit = unitsRepository.findByTrackingId(request.getUnitTrackingId())
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + request.getUnitTrackingId()));

        Agents agent = agentsMapper.toEntity(request, unit);
        Agents savedAgent = agentsRepository.save(agent);
        return agentsMapper.toResponse(savedAgent);
    }

    @Override
    public AgentsResponse update(UUID trackingId, AgentsRequest request) {
        Agents agent = agentsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + trackingId));

        Units unit = unitsRepository.findByTrackingId(request.getUnitTrackingId())
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + request.getUnitTrackingId()));

        agent.setRegistrationNo(request.getRegistrationNo());
        agent.setFirstName(request.getFirstName());
        agent.setLastName(request.getLastName());
        agent.setRank(request.getRank());
        agent.setAvailability(request.getAvailability());
        agent.setStatus(request.getStatus());
        agent.setUnit(unit);

        agent.setNationality(request.getNationality());
        agent.setCity(request.getCity());
        agent.setEmergencyContact(request.getEmergencyContact());
        agent.setMaritalStatus(request.getMaritalStatus());
        agent.setRecruitmentDate(request.getRecruitmentDate());
        agent.setContractEndDate(request.getContractEndDate());
        agent.setIdCardNumber(request.getIdCardNumber());
        agent.setPassportNumber(request.getPassportNumber());
        agent.setIdExpiryDate(request.getIdExpiryDate());
        agent.setInsuranceNumber(request.getInsuranceNumber());
        agent.setBankAccount(request.getBankAccount());

        Agents updatedAgent = agentsRepository.save(agent);
        return agentsMapper.toResponse(updatedAgent);
    }


    @Override
    @Transactional(readOnly = true)
    public AgentsResponse findByTrackingId(UUID trackingId) {
        Agents agent = agentsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + trackingId));
        return agentsMapper.toResponse(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentsResponse> findByStatus(AgentStatus status) {
        return agentsRepository.findByStatus(status)
                .stream()
                .map(agentsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentsResponse> findByUnit(UUID unitTrackingId) {
        Units unit = unitsRepository.findByTrackingId(unitTrackingId)
                .orElseThrow(() -> new RuntimeException("Unit not found with trackingId: " + unitTrackingId));
        return agentsRepository.findByUnit(unit)
                .stream()
                .map(agentsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentsResponse> findAll() {
        return agentsRepository.findAllWithUnit()
                .stream()
                .map(agentsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Agents agent = agentsRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Agent not found with trackingId: " + trackingId));
        agentsRepository.delete(agent);
    }
}
