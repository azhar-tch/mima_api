package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.UsersRequest;
import com.helpysoft.mima_api.dto.UsersResponse;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.entity.Rules;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsersMapper {

    public Users toEntity(UsersRequest request, Rules rule) {
        Users user = new Users();
        user.setTrackingId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setFcmToken(request.getFcmToken());
        user.setRule(rule);
        user.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        return user;
    }

    public UsersResponse toResponse(Users user) {
        UsersResponse response = new UsersResponse();
        response.setTrackingId(user.getTrackingId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setFcmToken(user.getFcmToken());
        response.setRuleTitle(user.getRule() != null ? user.getRule().getTitle() : null);
        response.setRuleTrackingId(user.getRule() != null ? user.getRule().getTrackingId() : null);
        response.setIsActive(user.getIsActive());
        response.setCreateDate(user.getCreateDate());
        response.setUpdateDate(user.getUpdateDate());
        return response;
    }
}
