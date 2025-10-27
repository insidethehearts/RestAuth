package me.therimuru.RestAuth.object;

import jakarta.validation.constraints.NotNull;

public record JWTRedisValue (
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