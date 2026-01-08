package me.therimuru.RestAuth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "subscriptions",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"subscriber_id", "target_id"}
        )
)
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @NonNull
    @Column(name = "subscriber_id", updatable = false, nullable = false)
    private Long subscriberId;

    @NonNull
    @Column(name = "target_id", updatable = false, nullable = false)
    private Long targetId;
}