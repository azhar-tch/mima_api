package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.ManagementRulesRequest;
import com.helpysoft.mima_api.dto.ManagementRulesResponse;
import com.helpysoft.mima_api.entity.ManagementRules;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ManagementRulesMapper {

    public ManagementRules toEntity(ManagementRulesRequest request) {
        ManagementRules rule = new ManagementRules();
        rule.setTrackingId(UUID.randomUUID());
        rule.setRuleName(request.getRuleName());
        rule.setPreventDoubleAssignment(request.getPreventDoubleAssignment());
        rule.setMinRestHours(request.getMinRestHours());
        rule.setMaxWeeklyHours(request.getMaxWeeklyHours());
        rule.setAutoReportUnjustifiedAbsences(request.getAutoReportUnjustifiedAbsences());
        rule.setEnforceEquityDistribution(request.getEnforceEquityDistribution());
        rule.setDescription(request.getDescription());
        rule.setEffectiveDate(request.getEffectiveDate() != null ? request.getEffectiveDate() : LocalDateTime.now());
        return rule;
    }

    public ManagementRulesResponse toResponse(ManagementRules rule) {
        ManagementRulesResponse response = new ManagementRulesResponse();
        response.setTrackingId(rule.getTrackingId());
        response.setRuleName(rule.getRuleName());
        response.setPreventDoubleAssignment(rule.getPreventDoubleAssignment());
        response.setMinRestHours(rule.getMinRestHours());
        response.setMaxWeeklyHours(rule.getMaxWeeklyHours());
        response.setAutoReportUnjustifiedAbsences(rule.getAutoReportUnjustifiedAbsences());
        response.setEnforceEquityDistribution(rule.getEnforceEquityDistribution());
        response.setDescription(rule.getDescription());
        response.setEffectiveDate(rule.getEffectiveDate());
        response.setCreateDate(rule.getCreateDate());
        response.setUpdateDate(rule.getUpdateDate());
        return response;
    }

    public void updateEntity(ManagementRules rule, ManagementRulesRequest request) {
        rule.setRuleName(request.getRuleName());
        rule.setPreventDoubleAssignment(request.getPreventDoubleAssignment());
        rule.setMinRestHours(request.getMinRestHours());
        rule.setMaxWeeklyHours(request.getMaxWeeklyHours());
        rule.setAutoReportUnjustifiedAbsences(request.getAutoReportUnjustifiedAbsences());
        rule.setEnforceEquityDistribution(request.getEnforceEquityDistribution());
        rule.setDescription(request.getDescription());
        if (request.getEffectiveDate() != null) {
            rule.setEffectiveDate(request.getEffectiveDate());
        }
    }
}
