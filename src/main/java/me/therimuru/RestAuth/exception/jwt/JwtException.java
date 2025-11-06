package me.therimuru.RestAuth.exception.jwt;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
