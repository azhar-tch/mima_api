package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.AbsencesRequest;
import com.helpysoft.mima_api.dto.AbsencesResponse;
import com.helpysoft.mima_api.entity.AbsenceStatus;
import com.helpysoft.mima_api.entity.Absences;
import com.helpysoft.mima_api.entity.Agents;
import com.helpysoft.mima_api.entity.Users;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
public class AbsencesMapper {

    /**
     * Calculate the number of days between start and end date (inclusive)
     * If start and end are on the same day, returns 1
     * @param startDate the start date of the absence
     * @param endDate the end date of the absence
     * @return the number of days
     */
    private long calculateNumberOfDays(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return 0L;
        }

        // Convert to LocalDate to get calendar days
        long daysBetween = ChronoUnit.DAYS.between(
            startDate.toLocalDate(),
            endDate.toLocalDate()
        );

        // Add 1 to include both start and end dates
        return daysBetween + 1;
    }

    public Absences toEntity(AbsencesRequest request, Agents agent, Users validatedBy) {
        Absences absence = new Absences();
        absence.setTrackingId(UUID.randomUUID());
        absence.setAbsenceType(request.getAbsenceType());
        absence.setStartDate(request.getStartDate());
        absence.setEndDate(request.getEndDate());
        absence.setJustification(request.getJustification());
        absence.setStatus(request.getStatus() != null ? request.getStatus() : AbsenceStatus.PENDING);
        absence.setReason(request.getReason());
        absence.setAgent(agent);
        absence.setValidatedBy(validatedBy);
        return absence;
    }

    public AbsencesResponse toResponse(Absences absence) {
        AbsencesResponse response = new AbsencesResponse();
        response.setTrackingId(absence.getTrackingId());
        response.setAbsenceType(absence.getAbsenceType());
        response.setStartDate(absence.getStartDate());
        response.setEndDate(absence.getEndDate());
        response.setNumberOfDays(calculateNumberOfDays(absence.getStartDate(), absence.getEndDate()));
        response.setJustification(absence.getJustification());
        response.setStatus(absence.getStatus());
        response.setReason(absence.getReason());

        // Ajouter les trackingIds pour les relations
        if (absence.getAgent() != null) {
            response.setAgentTrackingId(absence.getAgent().getTrackingId());
            response.setAgentName(absence.getAgent().getRegistrationNo() + " - " +
                (absence.getAgent().getFirstName() != null && absence.getAgent().getLastName() != null ?
                    absence.getAgent().getFirstName() + " " + absence.getAgent().getLastName() :
                    absence.getAgent().getFirstName() != null ? absence.getAgent().getFirstName() : "N/A"));
        }

        if (absence.getValidatedBy() != null) {
            response.setValidatedByTrackingId(absence.getValidatedBy().getTrackingId());
            response.setValidatedByName(absence.getValidatedBy().getFirstName() + " " +
                absence.getValidatedBy().getLastName());
        }

        response.setCreateDate(absence.getCreateDate());
        return response;
    }
}
