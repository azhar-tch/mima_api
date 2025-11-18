package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AgentsRequest;
import com.helpysoft.mima_api.dto.AgentsResponse;
import com.helpysoft.mima_api.dto.NotificationsRequest;
import com.helpysoft.mima_api.entity.*;
import com.helpysoft.mima_api.mapper.AgentsMapper;
import com.helpysoft.mima_api.repository.*;
import com.helpysoft.mima_api.service.AgentsService;
import com.helpysoft.mima_api.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AgentsServiceImpl implements AgentsService {

    private final AgentsRepository agentsRepository;
    private final UnitsRepository unitsRepository;
    private final AgentsMapper agentsMapper;
    private final AbsencesRepository absencesRepository;
    private final DutiesRepository dutiesRepository;
    private final MissionsRepository missionsRepository;
    private final NotificationsService notificationsService;

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

        // Informations maritimes
        agent.setMaritimeRank(request.getMaritimeRank());
        agent.setSpecialty(request.getSpecialty());
        agent.setSeafarerBookNumber(request.getSeafarerBookNumber());
        agent.setSeamanLicenseNumber(request.getSeamanLicenseNumber());
        agent.setMedicalCertificateExpiry(request.getMedicalCertificateExpiry());
        agent.setMaritimeQualifications(request.getMaritimeQualifications());
        agent.setQualifiedVesselType(request.getQualifiedVesselType());
        agent.setYearsOfSeaExperience(request.getYearsOfSeaExperience());
        agent.setLastSeaDutyDate(request.getLastSeaDutyDate());
        agent.setCertifications(request.getCertifications());

        agent.setAvailability(request.getAvailability());
        agent.setStatus(request.getStatus());
        agent.setUnit(unit);

        // Informations personnelles
        agent.setSex(request.getSex());
        agent.setDateOfBirth(request.getDateOfBirth());
        agent.setEmail(request.getEmail());
        agent.setPhoneNumber(request.getPhoneNumber());
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
    public List<AgentsResponse> findByStatus(MarinerStatus status) {
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
    @Transactional(readOnly = true)
    public List<AgentsResponse> searchAgents(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        return agentsRepository.searchAgents(searchTerm.trim())
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

    /**
     * Tâche planifiée qui met à jour automatiquement les statuts des agents
     * en fonction de leurs absences, gardes et missions actives.
     * Exécutée toutes les heures à la minute 0.
     */
    @Scheduled(cron = "0 0 * * * *") // Exécute toutes les heures à la minute 0
    @Transactional
    public void updateAgentStatuses() {
        LocalDateTime now = LocalDateTime.now();
        int updatedCount = 0;

        log.info("Démarrage de la mise à jour automatique des statuts des agents...");

        // Récupérer tous les agents
        List<Agents> allAgents = agentsRepository.findAll();

        for (Agents agent : allAgents) {
            MarinerStatus oldStatus = agent.getStatus();
            MarinerStatus newStatus = determineAgentStatus(agent, now);

            // Mettre à jour le statut si différent
            if (oldStatus != newStatus) {
                agent.setStatus(newStatus);
                agentsRepository.save(agent);
                updatedCount++;

                log.info("Agent {} {} - Statut mis à jour: {} → {}",
                        agent.getFirstName(), agent.getLastName(), oldStatus, newStatus);

                // Envoyer une notification à l'agent
                notifyAgentOfStatusChange(agent, oldStatus, newStatus);
            }
        }

        log.info("Mise à jour automatique des statuts des agents terminée. {} agent(s) mis à jour.", updatedCount);
    }

    /**
     * Détermine le statut d'un agent en fonction de ses absences, gardes et missions actives.
     * Priorité: ABSENT > EN_MER > EN_GARDE > DISPONIBLE
     */
    private MarinerStatus determineAgentStatus(Agents agent, LocalDateTime now) {
        // 1. Vérifier les absences approuvées actives (priorité la plus haute)
        List<Absences> activeAbsences = absencesRepository.findActiveAbsencesByAgent(
                agent, AbsenceStatus.APPROVED, now);
        if (!activeAbsences.isEmpty()) {
            return MarinerStatus.ABSENT;
        }

        // 2. Vérifier les missions en cours
        List<Missions> activeMissions = missionsRepository.findByAgentAndStatus(
                agent, MissionStatus.IN_PROGRESS);
        if (!activeMissions.isEmpty()) {
            return MarinerStatus.EN_MER;
        }

        // 3. Vérifier les gardes actives
        List<Duties> activeDuties = dutiesRepository.findByAgentAndStatus(
                agent, DutyStatus.ACTIVE);
        if (!activeDuties.isEmpty()) {
            return MarinerStatus.EN_GARDE;
        }

        // 4. Par défaut, l'agent est disponible
        return MarinerStatus.DISPONIBLE;
    }

    /**
     * Envoie une notification à l'agent lorsque son statut change automatiquement
     */
    private void notifyAgentOfStatusChange(Agents agent, MarinerStatus oldStatus, MarinerStatus newStatus) {
        try {
            String statusMessage;
            switch (newStatus) {
                case ABSENT:
                    statusMessage = "Votre statut a été automatiquement mis à jour à 'Absent' suite à votre absence approuvée";
                    break;
                case EN_MER:
                    statusMessage = "Votre statut a été automatiquement mis à jour à 'En mer' suite à votre affectation à une mission en cours";
                    break;
                case EN_GARDE:
                    statusMessage = "Votre statut a été automatiquement mis à jour à 'En garde' suite à votre affectation à une garde active";
                    break;
                case DISPONIBLE:
                    statusMessage = "Votre statut a été automatiquement mis à jour à 'Disponible'. Vous n'avez plus d'affectation active";
                    break;
                case PERMISSION:
                    statusMessage = "Votre statut a été automatiquement mis à jour à 'Permission'";
                    break;
                case EN_FORMATION:
                    statusMessage = "Votre statut a été automatiquement mis à jour à 'En formation'";
                    break;
                case INDISPONIBLE:
                    statusMessage = "Votre statut a été automatiquement mis à jour à 'Indisponible'";
                    break;
                default:
                    statusMessage = "Votre statut a été automatiquement mis à jour";
            }

            NotificationsRequest notificationRequest = new NotificationsRequest();
            notificationRequest.setMessage(statusMessage);
            notificationRequest.setNotificationType("agents");
            notificationRequest.setRecipientTrackingId(agent.getTrackingId());

            notificationsService.create(notificationRequest);

            log.info("✅ Notification de changement de statut envoyée à l'agent {} {}",
                    agent.getFirstName(), agent.getLastName());
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'envoi de la notification à l'agent {} {}: {}",
                    agent.getFirstName(), agent.getLastName(), e.getMessage());
        }
    }
}
