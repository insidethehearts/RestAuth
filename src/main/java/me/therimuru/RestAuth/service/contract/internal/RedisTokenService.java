package me.therimuru.RestAuth.service.contract.internal;

import me.therimuru.RestAuth.object.JwtRedisKey;

import java.time.Instant;

public interface RedisTokenService {

    void saveRefreshToken(JwtRedisKey jwtRedisKey);

    void saveRefreshToken(JwtRedisKey jwtRedisKey, Instant tokenExpirationInstant);

    JwtRedisKey getRefreshToken(Long userId);

    void invalidate(Long userId);
}