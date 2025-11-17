package com.helpysoft.mima_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RulesResponse {
    private UUID trackingId;
    private String title;
    private String description;
    private LocalDateTime createDate;
}
