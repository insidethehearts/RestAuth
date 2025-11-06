package me.therimuru.RestAuth.exception.jwt;

import me.therimuru.RestAuth.object.TokenType;

public class BadTokenTypeException extends JwtException {
    public BadTokenTypeException(TokenType tokenType) {
        super("Bad token type. Expected %s.".formatted(tokenType));
    }
}
