package me.therimuru.RestAuth.object;

import java.time.Instant;

public record JwtInformationWrapper(
   Long id,
   String username,
   TokenType tokenType,
   Instant issuedAt,
   Instant expiresAt
) {

    public JwtInformationWrapper(Long id, String username, TokenType tokenType) {
        this(id, username, tokenType, null, null);
    }

    @Override
    public String toString() {
        return "JwtInformationWrapper{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", tokenType=" + tokenType +
                ", issuedAt=" + issuedAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}