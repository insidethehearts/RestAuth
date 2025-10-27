package me.therimuru.RestAuth.exception.jwt.access;

public class ExpiredAccessJWTException extends RuntimeException {
    public ExpiredAccessJWTException() {
        super("Access token expired.");
    }
}
