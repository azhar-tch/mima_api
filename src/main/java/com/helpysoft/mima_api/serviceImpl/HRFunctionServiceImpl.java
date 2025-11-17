package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.dto.HRFunctionRequest;
import com.helpysoft.mima_api.dto.HRFunctionResponse;
import com.helpysoft.mima_api.mapper.HRFunctionMapper;
import com.helpysoft.mima_api.model.HRFunction;
import com.helpysoft.mima_api.repository.HRFunctionRepository;
import com.helpysoft.mima_api.service.HRFunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HRFunctionServiceImpl implements HRFunctionService {
    private final HRFunctionRepository hrFunctionRepository;
    private final HRFunctionMapper hrFunctionMapper;

    @Override
    public HRFunctionResponse create(HRFunctionRequest request) {
        if (hrFunctionRepository.existsByFunctionName(request.getFunctionName())) {
            throw new RuntimeException("Function with name '" + request.getFunctionName() + "' already exists");
        }
        HRFunction function = hrFunctionMapper.toEntity(request);
        HRFunction savedFunction = hrFunctionRepository.save(function);
        return hrFunctionMapper.toResponse(savedFunction);
    }

    @Override
    public HRFunctionResponse update(UUID trackingId, HRFunctionRequest request) {
        HRFunction function = hrFunctionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Function not found with trackingId: " + trackingId));
        hrFunctionMapper.updateEntity(function, request);
        HRFunction updatedFunction = hrFunctionRepository.save(function);
        return hrFunctionMapper.toResponse(updatedFunction);
    }

    @Override
    @Transactional(readOnly = true)
    public HRFunctionResponse findByTrackingId(UUID trackingId) {
        HRFunction function = hrFunctionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Function not found with trackingId: " + trackingId));
        return hrFunctionMapper.toResponse(function);
    }

    @Override
    @Transactional(readOnly = true)
    public HRFunctionResponse findByFunctionName(String functionName) {
        HRFunction function = hrFunctionRepository.findByFunctionName(functionName)
                .orElseThrow(() -> new RuntimeException("Function not found with name: " + functionName));
        return hrFunctionMapper.toResponse(function);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HRFunctionResponse> findByFunctionType(String functionType) {
        return hrFunctionRepository.findByFunctionType(functionType)
                .stream()
                .map(hrFunctionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HRFunctionResponse> findAll() {
        return hrFunctionRepository.findAll()
                .stream()
                .map(hrFunctionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        HRFunction function = hrFunctionRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Function not found with trackingId: " + trackingId));
        hrFunctionRepository.delete(function);
    }
}
