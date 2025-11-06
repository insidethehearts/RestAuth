package me.therimuru.RestAuth.exception.jwt.refresh;

import me.therimuru.RestAuth.exception.jwt.JwtException;

public class BadRefreshTokenException extends JwtException {
    public BadRefreshTokenException() {
        super("Bad refresh token.");
    }
}
