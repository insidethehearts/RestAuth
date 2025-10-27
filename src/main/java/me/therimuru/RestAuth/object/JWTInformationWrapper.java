package me.therimuru.RestAuth.object;

import java.time.Instant;

public record JWTInformationWrapper(
   Long id,
   String login,
   Instant issuedAt,
   Instant expiresAt
) {

    public JWTInformationWrapper(Long id, String login) {
        this(id, login, null, null);
    }

    @Override
    public String toString() {
        return "JWTInformationWrapper{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", issuedAt=" + issuedAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}