package me.therimuru.RestAuth.repository;

import me.therimuru.RestAuth.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, UUID> {

    Optional<SubscriptionEntity> findBySubscriberIdAndTargetId(Long subscriberId, Long targetId);

    List<SubscriptionEntity> findBySubscriberId(Long subscriberId);

    List<SubscriptionEntity> findByTargetId(Long targetId);

    void deleteBySubscriberIdAndTargetId(Long subscriberId, Long targetId);

}