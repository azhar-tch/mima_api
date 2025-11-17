package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.HRGradeRequest;
import com.helpysoft.mima_api.dto.HRGradeResponse;

import java.util.List;
import java.util.UUID;

public interface HRGradeService {
    HRGradeResponse create(HRGradeRequest request);
    HRGradeResponse update(UUID trackingId, HRGradeRequest request);
    HRGradeResponse findByTrackingId(UUID trackingId);
    HRGradeResponse findByGradeName(String gradeName);
    List<HRGradeResponse> findAllOrderedByHierarchy();
    List<HRGradeResponse> findAll();
    void delete(UUID trackingId);
}
