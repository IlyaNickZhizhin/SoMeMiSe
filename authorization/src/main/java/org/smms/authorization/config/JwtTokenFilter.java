package org.smms.authorization.config;

import java.io.IOException;

import org.slf4j.Logger;
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
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenFilter extends OncePerRequestFilter {
    
    JwtTokenProvider jwtTokenProvider;
    HandlerExceptionResolver resolver;
    Logger logger = org.slf4j.LoggerFactory.getLogger(JwtTokenFilter.class);

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, 
            @NotNull HttpServletResponse response, 
            @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String token = jwtTokenProvider.resolveToken(request);

        try {
            logger.info("Проверка токена {}", token);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                final Authentication auth = jwtTokenProvider.getAuthentification(token);
                if (auth != null) {
                    logger.info("Установка контекста аутентификации для пользователя {}, с ролью {}",
                            auth.getName(), auth.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Ошибка аутентификации пользователя");
            resolver.resolveException(request, response, null, e);
        }

        filterChain.doFilter(request, response);

    }
}
