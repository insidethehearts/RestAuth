package me.therimuru.RestAuth.object;

import jakarta.validation.constraints.NotNull;

public record JwtRedisKey(
        @NotNull Long userID,
        @NotNull String token
) {
    @Override
    public String toString() {
        return "JWTRedisValue{" +
                "userID=" + userID +
                ", token='" + token + '\'' +
                '}';
    }
}