package me.therimuru.RestAuth.service;

import me.therimuru.RestAuth.object.JwtInformationWrapper;

import java.time.Duration;
import java.time.Instant;

public interface JwtService {

    String generateAccessToken(JwtInformationWrapper information);

    JwtInformationWrapper decodeAccessToken(String token);

    String generateRefreshToken(JwtInformationWrapper information);

    JwtInformationWrapper decodeRefreshToken(String token);

    Instant getExpirationDate(String token);

    Duration getRefreshTokenDuration();
}