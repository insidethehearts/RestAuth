package me.therimuru.RestAuth.service.implementation.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.therimuru.RestAuth.object.JwtRedisKey;
import me.therimuru.RestAuth.service.contract.internal.RedisTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {

    private final RedisTemplate<Long, Object> redisTemplate;

    @Value("${app.jwt.refresh.duration}")
    private Integer refreshTokenDuration;
    @Value("${app.jwt.refresh.durationUnit}")
    private ChronoUnit refreshTokenDurationUnit;

    @Override
    public void saveRefreshToken(JwtRedisKey jwtRedisKey) {
        saveRefreshToken(jwtRedisKey, Instant.now().plus(Duration.of(refreshTokenDuration, refreshTokenDurationUnit)));
    }

    @Override
    public void saveRefreshToken(JwtRedisKey jwtRedisKey, Instant tokenExpirationInstant) {
        redisTemplate.opsForValue().set(jwtRedisKey.userID(), jwtRedisKey.token(), Duration.between(Instant.now(), tokenExpirationInstant));
    }

    @Override
    public JwtRedisKey getRefreshToken(Long userId) {
        return new JwtRedisKey(userId, (String) redisTemplate.opsForValue().get(userId));
    }

    @Override
    public void invalidate(Long userId) {
        redisTemplate.delete(userId);
    }
}
