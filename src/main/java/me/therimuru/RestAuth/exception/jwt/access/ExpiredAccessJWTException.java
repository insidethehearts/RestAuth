package me.therimuru.RestAuth.exception.jwt.access;

import me.therimuru.RestAuth.exception.jwt.JwtException;

public class ExpiredAccessJWTException extends JwtException {
    public ExpiredAccessJWTException() {
        super("Access token expired.");
    }
}
