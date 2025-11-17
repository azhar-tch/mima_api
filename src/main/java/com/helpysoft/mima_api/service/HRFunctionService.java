package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.HRFunctionRequest;
import com.helpysoft.mima_api.dto.HRFunctionResponse;

import java.util.List;
import java.util.UUID;

public interface HRFunctionService {
    HRFunctionResponse create(HRFunctionRequest request);
    HRFunctionResponse update(UUID trackingId, HRFunctionRequest request);
    HRFunctionResponse findByTrackingId(UUID trackingId);
    HRFunctionResponse findByFunctionName(String functionName);
    List<HRFunctionResponse> findByDepartment(String department);
    List<HRFunctionResponse> findByFunctionType(String functionType);
    List<HRFunctionResponse> searchByName(String functionName);
    List<HRFunctionResponse> findAll();
    void delete(UUID trackingId);
}
