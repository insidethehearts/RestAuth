package me.therimuru.RestAuth.service.implementation.internal;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.therimuru.RestAuth.exception.jwt.BadTokenTypeException;
import me.therimuru.RestAuth.object.JwtInformationWrapper;
import me.therimuru.RestAuth.object.TokenType;
import me.therimuru.RestAuth.service.contract.internal.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    private Algorithm signAlgorithm;
    private JWTVerifier baseTokenVerifier;

    @PostConstruct
    private void init() {
        signAlgorithm = Algorithm.HMAC512(secret);
        baseTokenVerifier = JWT
                .require(signAlgorithm)
                .withClaimPresence("id")
                .withClaimPresence("username")
                .withClaimPresence("iat")
                .withClaimPresence("exp")
                .build();
    }

    @Override
    public String generateToken(JwtInformationWrapper jwtInfo) {
        final TokenType tokenType = jwtInfo.tokenType();
        if (tokenType.equals(TokenType.ANY))
            throw new IllegalArgumentException("Expected tokenType to be ACCESS or REFRESH, but got: %s".formatted(tokenType));

        final Instant nowInstant = Instant.now();
        final Duration tokenDuration = tokenType.equals(TokenType.REFRESH) ? Duration.of(refreshTokenDuration, refreshTokenDurationUnit) : Duration.of(accessTokenDuration, accessTokenDurationUnit);
        return JWT
                .create()
                .withClaim("id", jwtInfo.id())
                .withClaim("username", jwtInfo.username())
                .withClaim("token_type", tokenType.toString())
                .withIssuedAt(jwtInfo.issuedAt() != null ? jwtInfo.issuedAt() : nowInstant)
                .withExpiresAt(jwtInfo.expiresAt() != null ? jwtInfo.expiresAt() : nowInstant.plus(tokenDuration))
                .sign(signAlgorithm);
    }

    @Override
    public JwtInformationWrapper decodeToken(String token, TokenType tokenType) {
        final DecodedJWT decodedJWT = decode(token, tokenType);
        return new JwtInformationWrapper(
                decodedJWT.getClaim("id").asLong(),
                decodedJWT.getClaim("username").asString(),
                TokenType.valueOf(decodedJWT.getClaim("token_type").asString()),
                decodedJWT.getIssuedAtAsInstant(),
                decodedJWT.getExpiresAtAsInstant()
        );
    }

    @Override
    public Instant getExpirationDate(String token) {
        DecodedJWT decodedJWT = decode(token, TokenType.ANY);
        return decodedJWT.getExpiresAtAsInstant();
    }

    private DecodedJWT decode(String token, TokenType tokenType) {
        DecodedJWT decodedJWT;
        decodedJWT = baseTokenVerifier.verify(token);
        if (!tokenType.equals(TokenType.ANY) && !decodedJWT.getClaim("token_type").asString().equalsIgnoreCase(tokenType.toString())) {
            throw new BadTokenTypeException(tokenType);
        }
        return decodedJWT;
    }
}