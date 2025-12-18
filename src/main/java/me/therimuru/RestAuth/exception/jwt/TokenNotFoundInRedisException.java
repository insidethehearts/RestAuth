package me.therimuru.RestAuth.exception.jwt;

public class TokenNotFoundInRedisException extends RuntimeException {
    public TokenNotFoundInRedisException() {
        super("Bad refresh token.");
    }
}
