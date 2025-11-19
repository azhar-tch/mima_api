package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.PersonnelAllowanceRequest;
import com.helpysoft.mima_api.dto.PersonnelAllowanceResponse;
import com.helpysoft.mima_api.entity.MaritimeRank;

import java.util.List;
import java.util.UUID;

public interface PersonnelAllowanceService {
    PersonnelAllowanceResponse create(PersonnelAllowanceRequest request);
    PersonnelAllowanceResponse update(UUID trackingId, PersonnelAllowanceRequest request);
    PersonnelAllowanceResponse findByTrackingId(UUID trackingId);
    PersonnelAllowanceResponse findByRankCode(String rankCode);
    PersonnelAllowanceResponse findByMaritimeRank(MaritimeRank maritimeRank);
    List<PersonnelAllowanceResponse> findActiveAllowances();
    List<PersonnelAllowanceResponse> findAllOrderByAllowance();
    List<PersonnelAllowanceResponse> findAll();
    List<PersonnelAllowanceResponse> searchPersonnelAllowances(String searchTerm);
    void delete(UUID trackingId);
}
