package org.smms.profile.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class JwtTokenFilter extends OncePerRequestFilter {
    
    JwtTokenProvider jwtTokenProvider;
    HandlerExceptionResolver resolver;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider,
                          @Qualifier("handlerExceptionResolver")HandlerExceptionResolver resolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.resolver = resolver;
    }

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
                 if ("true".equals(request.getHeader("X-Feign-Client"))) {
                     SecurityContextHolder.getContext().setAuthentication(
                         new UsernamePasswordAuthenticationToken(null, null,
                             Collections.singletonList(new SimpleGrantedAuthority("FEIGN_CLIENT"))));
                 }
            }
        } catch (JwtException | IllegalArgumentException e) {
            resolver.resolveException(request, response, null, e);
        }

        filterChain.doFilter(request, response);

    }
}
