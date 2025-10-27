package me.therimuru.RestAuth.service.implementation;

import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.object.JWTRedisValue;
import me.therimuru.RestAuth.service.RedisTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {

    private RedisTemplate<Long, Object> redisTemplate;

    @Override
    public void saveRefreshToken(JWTRedisValue jwtRedisValue, Instant tokenExpirationInstant) {
        redisTemplate.opsForValue().set(jwtRedisValue.userID(), jwtRedisValue.token(), Duration.between(Instant.now(), tokenExpirationInstant));
    }

    @Override
    public JWTRedisValue getRefreshToken(Long userId) {
        return (JWTRedisValue) redisTemplate.opsForValue().get(userId);
    }

    @Override
    public void invalidate(Long userId) {
        redisTemplate.delete(userId);
    }
}
