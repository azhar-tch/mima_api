package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.HRFunctionRequest;
import com.helpysoft.mima_api.dto.HRFunctionResponse;
import com.helpysoft.mima_api.entity.HRFunction;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HRFunctionMapper {

    public HRFunction toEntity(HRFunctionRequest request) {
        HRFunction function = new HRFunction();
        function.setTrackingId(UUID.randomUUID());
        function.setFunctionName(request.getFunctionName());
        function.setDescription(request.getDescription());
        function.setDepartment(request.getDepartment());
        return function;
    }

    public HRFunctionResponse toResponse(HRFunction function) {
        HRFunctionResponse response = new HRFunctionResponse();
        response.setTrackingId(function.getTrackingId());
        response.setFunctionName(function.getFunctionName());
        response.setDescription(function.getDescription());
        response.setDepartment(function.getDepartment());
        response.setCreateDate(function.getCreateDate());
        response.setUpdateDate(function.getUpdateDate());
        response.setCreatedBy(function.getCreatedBy());
        response.setUpdatedBy(function.getUpdatedBy());
        return response;
    }

    public void updateEntity(HRFunction function, HRFunctionRequest request) {
        function.setFunctionName(request.getFunctionName());
        function.setDescription(request.getDescription());
        function.setDepartment(request.getDepartment());
    }
}
