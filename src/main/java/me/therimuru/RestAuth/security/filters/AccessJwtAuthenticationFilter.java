package me.therimuru.RestAuth.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import me.therimuru.RestAuth.object.JwtInformationWrapper;
import me.therimuru.RestAuth.object.TokenType;
import me.therimuru.RestAuth.security.UserIdAuthentication;
import me.therimuru.RestAuth.service.contract.internal.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class AccessJwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = request.getHeader("Authorization");

        if (accessToken == null || accessToken.trim().isEmpty()) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final JwtInformationWrapper jwtInformation = jwtService.decodeToken(accessToken, TokenType.ACCESS);
            SecurityContextHolder.getContext().setAuthentication(new UserIdAuthentication(jwtInformation.id()));
        } catch (JWTVerificationException exception) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
