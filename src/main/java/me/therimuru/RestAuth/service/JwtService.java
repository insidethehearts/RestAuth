package me.therimuru.RestAuth.service;

import me.therimuru.RestAuth.object.JWTInformationWrapper;

public interface JwtService {

    String generateAccessJWT(JWTInformationWrapper jwtInformationWrapper);

    JWTInformationWrapper decodeAccessJWT(String jwt);

}