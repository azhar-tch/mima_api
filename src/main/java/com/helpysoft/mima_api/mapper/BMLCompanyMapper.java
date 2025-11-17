package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.BMLCompanyRequest;
import com.helpysoft.mima_api.dto.BMLCompanyResponse;
import com.helpysoft.mima_api.entity.BMLCompany;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BMLCompanyMapper {

    public BMLCompany toEntity(BMLCompanyRequest request) {
        BMLCompany company = new BMLCompany();
        company.setTrackingId(UUID.randomUUID());
        company.setCompanyName(request.getCompanyName());
        company.setDescription(request.getDescription());
        return company;
    }

    public BMLCompanyResponse toResponse(BMLCompany company) {
        BMLCompanyResponse response = new BMLCompanyResponse();
        response.setTrackingId(company.getTrackingId());
        response.setCompanyName(company.getCompanyName());
        response.setDescription(company.getDescription());
        response.setCreateDate(company.getCreateDate());
        response.setUpdateDate(company.getUpdateDate());
        response.setCreatedBy(company.getCreatedBy());
        response.setUpdatedBy(company.getUpdatedBy());
        return response;
    }

    public void updateEntity(BMLCompany company, BMLCompanyRequest request) {
        company.setCompanyName(request.getCompanyName());
        company.setDescription(request.getDescription());
    }
}
