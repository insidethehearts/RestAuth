package me.therimuru.RestAuth.exception.jwt.access;

public class BadAccessJWTException extends RuntimeException {
    public BadAccessJWTException() {
        super("Bad access token.");
    }
}
