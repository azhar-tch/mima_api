package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.BMLCompanyRequest;
import com.helpysoft.mima_api.dto.BMLCompanyResponse;

import java.util.List;
import java.util.UUID;

public interface BMLCompanyService {
    BMLCompanyResponse create(BMLCompanyRequest request);
    BMLCompanyResponse update(UUID trackingId, BMLCompanyRequest request);
    BMLCompanyResponse findByTrackingId(UUID trackingId);
    BMLCompanyResponse findByCompanyName(String companyName);
    List<BMLCompanyResponse> searchByName(String companyName);
    List<BMLCompanyResponse> findAll();
    void delete(UUID trackingId);
}
