package me.therimuru.RestAuth.service.implementation;

import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.object.JwtRedisKey;
import me.therimuru.RestAuth.service.JwtService;
import me.therimuru.RestAuth.service.RedisTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {

    private RedisTemplate<Long, Object> redisTemplate;
    private JwtService jwtService;

    @Override
    public void saveRefreshToken(JwtRedisKey jwtRedisKey) {
        saveRefreshToken(jwtRedisKey, Instant.now().plus(jwtService.getRefreshTokenDuration()));
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
