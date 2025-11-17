package com.helpysoft.mima_api.service;

import com.helpysoft.mima_api.dto.UsersRequest;
import com.helpysoft.mima_api.dto.UsersResponse;

import java.util.List;
import java.util.UUID;

public interface UsersService {
    UsersResponse create(UsersRequest request);
    UsersResponse update(UUID trackingId, UsersRequest request);
    UsersResponse findByTrackingId(UUID trackingId);
    UsersResponse findByEmail(String email);
    List<UsersResponse> findAll();
    void delete(UUID trackingId);
}
