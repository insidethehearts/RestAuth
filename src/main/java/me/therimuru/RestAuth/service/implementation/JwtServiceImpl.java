package me.therimuru.RestAuth.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.therimuru.RestAuth.exception.jwt.BadTokenTypeException;
import me.therimuru.RestAuth.exception.jwt.access.BadAccessJWTException;
import me.therimuru.RestAuth.exception.jwt.access.ExpiredAccessJWTException;
import me.therimuru.RestAuth.object.JWTInformationWrapper;
import me.therimuru.RestAuth.object.TokenType;
import me.therimuru.RestAuth.service.JwtService;
import me.therimuru.RestAuth.service.RedisTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access.duration}")
    private Integer accessTokenDuration;
    @Value("${app.jwt.access.durationUnit}")
    private ChronoUnit accessTokenDurationUnit;
    @Value("${app.jwt.refresh.duration}")
    private Integer refreshTokenDuration;
    @Value("${app.jwt.refresh.durationUnit}")
    private ChronoUnit refreshTokenDurationUnit;

    private final RedisTokenService redisTokenService;

    private Algorithm signAlgorithm;
    private JWTVerifier jwtVerifier;

    @PostConstruct
    private void init() {
        signAlgorithm = Algorithm.HMAC512(secret);
        jwtVerifier = JWT
                .require(signAlgorithm)
                .withClaimPresence("id")
                .withClaimPresence("login")
                .withClaimPresence("iat")
                .withClaimPresence("exp")
                .withArrayClaim("token_type", new String[]{"ACCESS", "REFRESH"})
                .build();
    }

    public String generateAccessToken(JWTInformationWrapper jwtInformationWrapper) {
        final Instant nowInstant = Instant.now();
        return JWT
                .create()
                .withClaim("id", jwtInformationWrapper.id())
                .withClaim("login", jwtInformationWrapper.login())
                .withIssuedAt(jwtInformationWrapper.issuedAt() != null ? jwtInformationWrapper.issuedAt() : nowInstant)
                .withExpiresAt(jwtInformationWrapper.expiresAt() != null ? jwtInformationWrapper.expiresAt() : nowInstant.plus(accessTokenDuration, accessTokenDurationUnit))
                .sign(signAlgorithm);
    }

    public JWTInformationWrapper decodeAccessToken(String token) {
        final DecodedJWT decodedJWT = decodeToken(token, TokenType.ACCESS);
        return new JWTInformationWrapper(
                decodedJWT.getClaim("id").asLong(),
                decodedJWT.getClaim("login").asString(),
                decodedJWT.getIssuedAtAsInstant(),
                decodedJWT.getExpiresAtAsInstant()
        );
    }

    @Override
    public String generateRefreshToken(JWTInformationWrapper jwtInformationWrapper) {
        final Instant nowInstant = Instant.now();
        return JWT
                .create()
                .withClaim("id", jwtInformationWrapper.id())
                .withClaim("login", jwtInformationWrapper.login())
                .withIssuedAt(jwtInformationWrapper.issuedAt() != null ? jwtInformationWrapper.issuedAt() : nowInstant)
                .withExpiresAt(jwtInformationWrapper.expiresAt() != null ? jwtInformationWrapper.expiresAt() : nowInstant.plus(refreshTokenDuration, refreshTokenDurationUnit))
                .sign(signAlgorithm);
    }

    @Override
    public JWTInformationWrapper decodeRefreshToken(String token) {
        final DecodedJWT decodedJWT = decodeToken(token, TokenType.REFRESH);
        return new JWTInformationWrapper(
                decodedJWT.getClaim("id").asLong(),
                decodedJWT.getClaim("login").asString(),
                decodedJWT.getIssuedAtAsInstant(),
                decodedJWT.getExpiresAtAsInstant()
        );
    }

    @Override
    public void invalidateRefreshToken(Long userId) {
        redisTokenService.invalidate(userId);
    }

    @Override
    public Instant getExpirationDate(String token) {
        DecodedJWT decodedJWT = decodeToken(token, TokenType.ANY);
        return decodedJWT.getExpiresAtAsInstant();
    }

    private DecodedJWT decodeToken(String token, TokenType tokenType) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = jwtVerifier.verify(token);
        } catch (TokenExpiredException exception) {
            throw new ExpiredAccessJWTException();
        }  catch (JWTVerificationException exception) {
            throw new BadAccessJWTException();
        }
        if (!tokenType.equals(TokenType.ANY) && decodedJWT.getClaim("token_type").asString().equals(tokenType.toString())) {
            throw new BadTokenTypeException(tokenType);
        }
        return decodedJWT;
    }
}