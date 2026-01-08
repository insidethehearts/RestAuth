package me.therimuru.RestAuth.exception.subscription;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(Long subscriberId, Long targetId) {
        super("User with id %s not subscribed to user with id %s.".formatted(subscriberId, targetId));
    }
}
