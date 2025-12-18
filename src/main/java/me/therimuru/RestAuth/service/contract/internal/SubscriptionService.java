package me.therimuru.RestAuth.service.contract.internal;

import me.therimuru.RestAuth.entity.SubscriptionEntity;

import java.util.List;

public interface SubscriptionService {

    SubscriptionEntity subscribe(Long subscriberId, Long targetId);

    SubscriptionEntity getSubscribe(Long subscriberId, Long targetId);

    List<SubscriptionEntity> getSubscribers(Long targetId);

    List<SubscriptionEntity> getSubscriptions(Long subscriberId);

    void unsubscribe(Long subscriberId, Long targetId);

}