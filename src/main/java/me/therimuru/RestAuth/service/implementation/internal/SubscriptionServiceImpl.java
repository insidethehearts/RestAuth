package me.therimuru.RestAuth.service.implementation.internal;

import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.entity.SubscriptionEntity;
import me.therimuru.RestAuth.exception.database.UserForSubscribeNotFoundException;
import me.therimuru.RestAuth.exception.subscription.CantSubscribeYourselfException;
import me.therimuru.RestAuth.exception.subscription.SubscriptionNotFoundException;
import me.therimuru.RestAuth.repository.SubscriptionRepository;
import me.therimuru.RestAuth.service.contract.internal.SubscriptionService;
import me.therimuru.RestAuth.service.contract.internal.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private SubscriptionRepository repository;
    private UserService userService;

    @Override
    public SubscriptionEntity subscribe(Long subscriberId, Long targetId) {
        if (Objects.equals(subscriberId, targetId)) throw new CantSubscribeYourselfException();
        if (!userService.existsById(targetId)) throw new UserForSubscribeNotFoundException(targetId);
        return repository.save(new SubscriptionEntity(subscriberId, targetId));
    }

    @Override
    public SubscriptionEntity getSubscribe(Long subscriberId, Long targetId) {
        return repository.findBySubscriberIdAndTargetId(subscriberId, targetId).orElseThrow(() -> new SubscriptionNotFoundException(subscriberId, targetId));
    }

    @Override
    public List<SubscriptionEntity> getSubscribers(Long targetId) {
        return repository.findByTargetId(targetId);
    }

    @Override
    public List<SubscriptionEntity> getSubscriptions(Long subscriberId) {
        return repository.findBySubscriberId(subscriberId);
    }

    @Override
    public boolean isSubscribed(Long subscriberId, Long profileId) {
        final Optional<SubscriptionEntity> subscription = repository.findBySubscriberIdAndTargetId(subscriberId, profileId);
        return subscription.isPresent();
    }

    @Override
    public void unsubscribe(Long subscriberId, Long targetId) {
        repository.deleteBySubscriberIdAndTargetId(subscriberId, targetId);
    }
}
