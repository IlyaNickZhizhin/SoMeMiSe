package org.smms.authorization.config;

import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.smms.authorization.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Используется для создания и валидации JWT токенов
 */
@Component
@Configuration
@RequiredArgsConstructor
public class JwtTokenProvider {

    Logger logger = org.slf4j.LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private long validityInMilliseconds;

    private final UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * Создает токен для пользователя
     * @param login имя пользователя {@link UserDto}
     * @param role роль пользователя {@link UserDto}
     * @return токен {@link String}
     */
    public String createToken(String login, String role) {

        final Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role);

        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    /**
     * Возвращает аутентификацию пользователя по токену
     * @param token токен, сгенерированный системой {@link String}
     * @return данные аутентификация пользователя {@link Authentication}
     */
    public Authentication getAuthentification(String token) {
        final String login = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        final UserDetails user = userDetailsService.loadUserByUsername(login);
                
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    /** 
     * @param request http-запрос {@link HttpServletRequest}
     * @return в случае когда в заголовке есть токен, возвращает его (без префикса "Bearer_"),
     * иначе null {@link String}
     */
    public String resolveToken(HttpServletRequest request) {
        
        final String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }

    /**
     * @param token сгераованный системой {@link String}
     * @return true, если токен проходит валидацию, иначе false {@link Boolean}
     * @throws ExpiredJwtException
     */
    public boolean validateToken(String token) throws ExpiredJwtException {
        final Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

}
