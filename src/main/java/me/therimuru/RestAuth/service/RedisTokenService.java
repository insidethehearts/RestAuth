package me.therimuru.RestAuth.service;

import me.therimuru.RestAuth.object.JWTRedisValue;

import java.time.Instant;

public interface RedisTokenService {

    void saveRefreshToken(JWTRedisValue jwtRedisValue, Instant tokenExpirationInstant);

    JWTRedisValue getRefreshToken(Long userId);

    void invalidate(Long userId);
}