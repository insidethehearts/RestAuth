package me.therimuru.RestAuth.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import me.therimuru.RestAuth.exception.BadAccessJWTException;
import me.therimuru.RestAuth.object.JWTInformationWrapper;
import me.therimuru.RestAuth.service.JwtService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    // todo: make secret
    private Algorithm signAlgorithm = Algorithm.HMAC512("PUT_SECRET_HERE");
    private JWTVerifier jwtVerifier = JWT.require(signAlgorithm).build();

    public String generateAccessJWT(JWTInformationWrapper jwtInformationWrapper) {
        return JWT
                .create()
                .withClaim("id", jwtInformationWrapper.id())
                .withClaim("login", jwtInformationWrapper.login())
                .sign(signAlgorithm);
    }

    public JWTInformationWrapper decodeAccessJWT(String jwt) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = jwtVerifier.verify(jwt);
        } catch (JWTVerificationException e) {
            throw new BadAccessJWTException();
        }
        return new JWTInformationWrapper(
                decodedJWT.getClaim("id").asLong(),
                decodedJWT.getClaim("login").asString()
        );
    }

}