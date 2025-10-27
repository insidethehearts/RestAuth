package me.therimuru.RestAuth.service;

import me.therimuru.RestAuth.object.JWTInformationWrapper;

import java.time.Instant;

public interface JwtService {

    String generateAccessToken(JWTInformationWrapper information);

    JWTInformationWrapper decodeAccessToken(String token);

    String generateRefreshToken(JWTInformationWrapper information);

    JWTInformationWrapper decodeRefreshToken(String token);

    void invalidateRefreshToken(Long userId);

    Instant getExpirationDate(String token);
}