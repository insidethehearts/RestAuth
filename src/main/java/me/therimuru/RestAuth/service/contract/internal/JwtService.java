package me.therimuru.RestAuth.service.contract.internal;

import me.therimuru.RestAuth.object.JwtInformationWrapper;
import me.therimuru.RestAuth.object.TokenType;

import java.time.Instant;

public interface JwtService {

    String generateToken(JwtInformationWrapper jwtInfo);

    JwtInformationWrapper decodeToken(String token, TokenType tokenType);

    Instant getExpirationDate(String token);
}