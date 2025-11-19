package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.AbsencesRequest;
import com.helpysoft.mima_api.dto.AbsencesResponse;
import com.helpysoft.mima_api.entity.AbsenceStatus;
import com.helpysoft.mima_api.entity.AbsenceType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AbsencesService {
    AbsencesResponse create(AbsencesRequest request);
    AbsencesResponse update(UUID trackingId, AbsencesRequest request);
    AbsencesResponse findByTrackingId(UUID trackingId);
    List<AbsencesResponse> findByAgent(UUID agentTrackingId);
    List<AbsencesResponse> findByStatus(AbsenceStatus status);
    List<AbsencesResponse> findByAbsenceType(AbsenceType absenceType);
    List<AbsencesResponse> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<AbsencesResponse> findAll();
    List<AbsencesResponse> searchAbsences(String searchTerm);
    AbsencesResponse updateStatus(UUID trackingId, AbsenceStatus status, UUID validatedByTrackingId);
    void delete(UUID trackingId);
}
