package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.config.CustomUserDetails;
import com.helpysoft.mima_api.dto.UsersRequest;
import com.helpysoft.mima_api.dto.UsersResponse;
import com.helpysoft.mima_api.entity.Rules;
import com.helpysoft.mima_api.entity.Users;
import com.helpysoft.mima_api.mapper.UsersMapper;
import com.helpysoft.mima_api.repository.RulesRepository;
import com.helpysoft.mima_api.repository.UsersRepository;
import com.helpysoft.mima_api.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersServiceImpl implements UsersService, UserDetailsService {

    private final UsersRepository usersRepository;
    private final RulesRepository rulesRepository;
    private final UsersMapper usersMapper;

    @Override
    public UsersResponse create(UsersRequest request) {
        Rules rule = rulesRepository.findByTrackingId(request.getRuleTrackingId())
                .orElseThrow(() -> new RuntimeException("Rule not found with trackingId: " + request.getRuleTrackingId()));

        Users user = usersMapper.toEntity(request, rule);
        Users savedUser = usersRepository.save(user);
        return usersMapper.toResponse(savedUser);
    }

    @Override
    public UsersResponse update(UUID trackingId, UsersRequest request) {
        Users user = usersRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("User not found with trackingId: " + trackingId));

        Rules rule = rulesRepository.findByTrackingId(request.getRuleTrackingId())
                .orElseThrow(() -> new RuntimeException("Rule not found with trackingId: " + request.getRuleTrackingId()));

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setFcmToken(request.getFcmToken());
        user.setRule(rule);
        user.setIsActive(request.getIsActive());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }

        Users updatedUser = usersRepository.save(user);
        return usersMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UsersResponse findByTrackingId(UUID trackingId) {
        Users user = usersRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("User not found with trackingId: " + trackingId));
        return usersMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UsersResponse findByEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return usersMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsersResponse> findAll() {
        return usersRepository.findAll()
                .stream()
                .map(usersMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID trackingId) {
        Users user = usersRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("User not found with trackingId: " + trackingId));
        usersRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String role = user.getRule() != null ? user.getRule().getTitle() : "USER";
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        return new CustomUserDetails(user, Collections.singletonList(authority));
    }
}
