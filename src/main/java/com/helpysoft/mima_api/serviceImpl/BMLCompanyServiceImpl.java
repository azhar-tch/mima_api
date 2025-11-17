package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.BMLCompanyRequest;
import com.helpysoft.mima_api.dto.BMLCompanyResponse;
import com.helpysoft.mima_api.mapper.BMLCompanyMapper;
import com.helpysoft.mima_api.entity.BMLCompany;
import com.helpysoft.mima_api.repository.BMLCompanyRepository;
import com.helpysoft.mima_api.service.BMLCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BMLCompanyServiceImpl implements BMLCompanyService {
    private final BMLCompanyRepository bmlCompanyRepository;
    private final BMLCompanyMapper bmlCompanyMapper;

    @Override
    public BMLCompanyResponse create(BMLCompanyRequest request) {
        if (bmlCompanyRepository.existsByCompanyName(request.getCompanyName())) {
            throw new RuntimeException("Company with name '" + request.getCompanyName() + "' already exists");
        }
        BMLCompany company = bmlCompanyMapper.toEntity(request);
        BMLCompany savedCompany = bmlCompanyRepository.save(company);
        return bmlCompanyMapper.toResponse(savedCompany);
    }

    @Override
    public BMLCompanyResponse update(UUID trackingId, BMLCompanyRequest request) {
        BMLCompany company = bmlCompanyRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Company not found with trackingId: " + trackingId));
        bmlCompanyMapper.updateEntity(company, request);
        BMLCompany updatedCompany = bmlCompanyRepository.save(company);
        return bmlCompanyMapper.toResponse(updatedCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public BMLCompanyResponse findByTrackingId(UUID trackingId) {
        BMLCompany company = bmlCompanyRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Company not found with trackingId: " + trackingId));
        return bmlCompanyMapper.toResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public BMLCompanyResponse findByCompanyName(String companyName) {
        BMLCompany company = bmlCompanyRepository.findByCompanyName(companyName)
                .orElseThrow(() -> new RuntimeException("Company not found with name: " + companyName));
        return bmlCompanyMapper.toResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BMLCompanyResponse> searchByName(String companyName) {
        return bmlCompanyRepository.findByCompanyNameContainingIgnoreCase(companyName)
                .stream()
                .map(bmlCompanyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BMLCompanyResponse> findAll() {
        return bmlCompanyRepository.findAll()
                .stream()
                .map(bmlCompanyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        BMLCompany company = bmlCompanyRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Company not found with trackingId: " + trackingId));
        bmlCompanyRepository.delete(company);
    }
}
