package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.AwardRequest;
import com.helpysoft.mima_api.dto.AwardResponse;
import com.helpysoft.mima_api.mapper.AwardMapper;
import com.helpysoft.mima_api.model.Award;
import com.helpysoft.mima_api.repository.AwardRepository;
import com.helpysoft.mima_api.service.AwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AwardServiceImpl implements AwardService {
    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;

    @Override
    public AwardResponse create(AwardRequest request) {
        if (awardRepository.existsByAwardName(request.getAwardName())) {
            throw new RuntimeException("Award with name '" + request.getAwardName() + "' already exists");
        }
        Award award = awardMapper.toEntity(request);
        Award savedAward = awardRepository.save(award);
        return awardMapper.toResponse(savedAward);
    }

    @Override
    public AwardResponse update(UUID trackingId, AwardRequest request) {
        Award award = awardRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Award not found with trackingId: " + trackingId));
        awardMapper.updateEntity(award, request);
        Award updatedAward = awardRepository.save(award);
        return awardMapper.toResponse(updatedAward);
    }

    @Override
    @Transactional(readOnly = true)
    public AwardResponse findByTrackingId(UUID trackingId) {
        Award award = awardRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Award not found with trackingId: " + trackingId));
        return awardMapper.toResponse(award);
    }

    @Override
    @Transactional(readOnly = true)
    public AwardResponse findByAwardName(String awardName) {
        Award award = awardRepository.findByAwardName(awardName)
                .orElseThrow(() -> new RuntimeException("Award not found with name: " + awardName));
        return awardMapper.toResponse(award);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AwardResponse> findByAwardType(String awardType) {
        return awardRepository.findByAwardType(awardType)
                .stream()
                .map(awardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AwardResponse> findAll() {
        return awardRepository.findAll()
                .stream()
                .map(awardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Award award = awardRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Award not found with trackingId: " + trackingId));
        awardRepository.delete(award);
    }
}
