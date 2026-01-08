package me.therimuru.RestAuth.service.implementation.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.therimuru.RestAuth.entity.UserEntity;
import me.therimuru.RestAuth.exception.profile.UserProfileNotFoundException;
import me.therimuru.RestAuth.security.UserIdAuthentication;
import me.therimuru.RestAuth.service.contract.adapter.ProfileService;
import me.therimuru.RestAuth.service.contract.internal.SubscriptionService;
import me.therimuru.RestAuth.service.contract.internal.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    private final ObjectMapper userMapper;

    @Override
    public Map<String, Object> getProfileInfo(Long targetId, UserIdAuthentication authentication) {
        final Optional<UserEntity> optTarget = userService.findByIdSafe(targetId);
        if (optTarget.isEmpty()) throw new UserProfileNotFoundException(targetId);
        final UserEntity target = optTarget.get();

        final Map<String, Object> response = new HashMap<>();
        response.put("id", target.getId());
        response.put("name", target.getName());
        response.put("surname", target.getSurname());
        response.put("age", target.getAge());

        if (Objects.isNull(target.getBio())) return response;

        if (target.getBioPublic()) {
            response.put("bio", target.getBio());
        } else {
            final UserEntity requester = userService.findByIdSafe(authentication.getId()).orElse(null);
            if (Objects.isNull(requester)) return response;

            if (subscriptionService.isSubscribed(targetId, authentication.getId())) {
                response.put("bio", target.getBio());
            }
        }

        return response;
    }

    @Override
    @SneakyThrows
    public Map<String, Object> getSelfProfileInfo(Long id){
        final Optional<UserEntity> optTarget = userService.findByIdSafe(id);
        if (optTarget.isEmpty()) throw new UserProfileNotFoundException(id);
        final UserEntity target = optTarget.get();

        return userMapper.convertValue(target, new TypeReference<Map<String, Object>>() {});
    }
}