package com.helpysoft.mima_api.mapper;

import com.helpysoft.mima_api.dto.HRGradeRequest;
import com.helpysoft.mima_api.dto.HRGradeResponse;
import com.helpysoft.mima_api.entity.HRGrade;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HRGradeMapper {

    public HRGrade toEntity(HRGradeRequest request) {
        HRGrade grade = new HRGrade();
        grade.setTrackingId(UUID.randomUUID());
        grade.setGradeName(request.getGradeName());
        grade.setDescription(request.getDescription());
        grade.setHierarchyLevel(request.getHierarchyLevel());
        return grade;
    }

    public HRGradeResponse toResponse(HRGrade grade) {
        HRGradeResponse response = new HRGradeResponse();
        response.setTrackingId(grade.getTrackingId());
        response.setGradeName(grade.getGradeName());
        response.setDescription(grade.getDescription());
        response.setHierarchyLevel(grade.getHierarchyLevel());
        response.setCreateDate(grade.getCreateDate());
        response.setUpdateDate(grade.getUpdateDate());
        response.setCreatedBy(grade.getCreatedBy());
        response.setUpdatedBy(grade.getUpdatedBy());
        return response;
    }

    public void updateEntity(HRGrade grade, HRGradeRequest request) {
        grade.setGradeName(request.getGradeName());
        grade.setDescription(request.getDescription());
        grade.setHierarchyLevel(request.getHierarchyLevel());
    }
}
