package org.smms.authorization.config;

import java.io.IOException;

import lombok.AccessLevel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenFilter extends OncePerRequestFilter {
    
    JwtTokenProvider jwtTokenProvider;
    HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, 
            @NotNull HttpServletResponse response, 
            @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String token = jwtTokenProvider.resolveToken(request);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                final Authentication auth = jwtTokenProvider.getAuthentification(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (JwtException | IllegalArgumentException e) {
            resolver.resolveException(request, response, null, e);
        }

        filterChain.doFilter(request, response);

    }
}
