package com.helpysoft.mima_api.dto;

import com.helpysoft.mima_api.entity.MaritimeRank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelAllowanceResponse {
    private UUID trackingId;
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
    private LocalDateTime createDate;
}
