package com.helpysoft.mima_api.serviceImpl;

import com.helpysoft.mima_api.service.JwtBlacklistService;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtBlacklistServiceImpl implements JwtBlacklistService {
    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    @Override
    public void addToBlacklist(String token, long expirationTime) {
        long expirationTimestamp = System.currentTimeMillis() + expirationTime;
        blacklistedTokens.put(token, expirationTimestamp);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token) &&
                blacklistedTokens.get(token) > System.currentTimeMillis();
    }
}
