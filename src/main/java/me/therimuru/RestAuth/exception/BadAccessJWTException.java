package me.therimuru.RestAuth.exception;

public class BadAccessJWTException extends RuntimeException {
    public BadAccessJWTException() {
        super("Bad access token.");
    }
}
