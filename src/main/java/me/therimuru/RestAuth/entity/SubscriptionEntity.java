package me.therimuru.RestAuth.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "subscriptions",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"subscriberId", "targetId"}
        )
)
@RequiredArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "subscriber_id", updatable = false, nullable = false)
    private final Long subscriberId;

    @Column(name = "target_id", updatable = false, nullable = false)
    private final Long targetId;
}