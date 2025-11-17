package com.helpysoft.mima_api.service;

public interface JwtBlacklistService {
    void addToBlacklist(String token, long expirationTime);
    boolean isBlacklisted(String token);
}
