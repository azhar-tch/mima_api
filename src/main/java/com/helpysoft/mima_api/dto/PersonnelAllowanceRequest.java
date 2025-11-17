package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.MaritimeRank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PersonnelAllowanceRequest {
    private String rankCode;
    private MaritimeRank maritimeRank;
    private BigDecimal escortDailyAllowance;
    private BigDecimal armedGuardDailyAllowance;
    private BigDecimal patrolAllowance;
    private BigDecimal riskAllowance;
    private BigDecimal seaAllowance;
    private String currency;
    private String observations;
    private Boolean isActive;
}
