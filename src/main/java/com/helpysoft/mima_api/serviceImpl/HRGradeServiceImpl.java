package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.HRGradeRequest;
import com.helpysoft.mima_api.dto.HRGradeResponse;
import com.helpysoft.mima_api.mapper.HRGradeMapper;
import com.helpysoft.mima_api.model.HRGrade;
import com.helpysoft.mima_api.repository.HRGradeRepository;
import com.helpysoft.mima_api.service.HRGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HRGradeServiceImpl implements HRGradeService {
    private final HRGradeRepository hrGradeRepository;
    private final HRGradeMapper hrGradeMapper;

    @Override
    public HRGradeResponse create(HRGradeRequest request) {
        if (hrGradeRepository.existsByGradeName(request.getGradeName())) {
            throw new RuntimeException("Grade with name '" + request.getGradeName() + "' already exists");
        }
        HRGrade grade = hrGradeMapper.toEntity(request);
        HRGrade savedGrade = hrGradeRepository.save(grade);
        return hrGradeMapper.toResponse(savedGrade);
    }

    @Override
    public HRGradeResponse update(UUID trackingId, HRGradeRequest request) {
        HRGrade grade = hrGradeRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Grade not found with trackingId: " + trackingId));
        hrGradeMapper.updateEntity(grade, request);
        HRGrade updatedGrade = hrGradeRepository.save(grade);
        return hrGradeMapper.toResponse(updatedGrade);
    }

    @Override
    @Transactional(readOnly = true)
    public HRGradeResponse findByTrackingId(UUID trackingId) {
        HRGrade grade = hrGradeRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Grade not found with trackingId: " + trackingId));
        return hrGradeMapper.toResponse(grade);
    }

    @Override
    @Transactional(readOnly = true)
    public HRGradeResponse findByGradeName(String gradeName) {
        HRGrade grade = hrGradeRepository.findByGradeName(gradeName)
                .orElseThrow(() -> new RuntimeException("Grade not found with name: " + gradeName));
        return hrGradeMapper.toResponse(grade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HRGradeResponse> findAllOrderedByHierarchy() {
        return hrGradeRepository.findAllByOrderByHierarchyLevelAsc()
                .stream()
                .map(hrGradeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HRGradeResponse> findAll() {
        return hrGradeRepository.findAll()
                .stream()
                .map(hrGradeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        HRGrade grade = hrGradeRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Grade not found with trackingId: " + trackingId));
        hrGradeRepository.delete(grade);
    }
}
