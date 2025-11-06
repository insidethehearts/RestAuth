package me.therimuru.RestAuth.exception.jwt.access;

import me.therimuru.RestAuth.exception.jwt.JwtException;

public class BadAccessJWTException extends JwtException {
    public BadAccessJWTException() {
        super("Bad access token.");
    }
}
