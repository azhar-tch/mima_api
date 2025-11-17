package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.RulesRequest;
import com.helpysoft.mima_api.dto.RulesResponse;
import com.helpysoft.mima_api.entity.Rules;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RulesMapper {

    public Rules toEntity(RulesRequest request) {
        Rules rule = new Rules();
        rule.setTrackingId(UUID.randomUUID());
        rule.setTitle(request.getTitle());
        rule.setDescription(request.getDescription());
        return rule;
    }

    public RulesResponse toResponse(Rules rule) {
        RulesResponse response = new RulesResponse();
        response.setTrackingId(rule.getTrackingId());
        response.setTitle(rule.getTitle());
        response.setDescription(rule.getDescription());
        response.setCreateDate(rule.getCreateDate());
        return response;
    }
}
