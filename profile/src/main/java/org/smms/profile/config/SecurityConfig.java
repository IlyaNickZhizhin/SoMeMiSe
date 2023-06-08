package org.smms.profile.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String[] AUTH_ALOWEDLIST = {
        "/registration",
        "/read/**"
    };

    // TODO Настройить нормально доступ feign к микросервису
    // private static final String[] FEIGN = {
    //     "/read/**",
    // };

    private static final String[] AUTH_ALOWEDLIST_SWAGGER = {
        "/v3/api-docs",
        "/configuration/ui",
        "/swagger-resources/**",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        "/v2/api-docs",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/api-docs/**",
        "api-docs" 
    };


    private final JwtTokenProvider jwtTokenProvider;
    private final HandlerExceptionResolver resolver;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, 
                @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.resolver = resolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic((htp) -> htp.disable())
                .csrf(cr ->
                    cr.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((request) -> request
                    .requestMatchers(AUTH_ALOWEDLIST).permitAll()
                    .requestMatchers(AUTH_ALOWEDLIST_SWAGGER).permitAll()
                    .anyRequest().permitAll()
                )
                .addFilterBefore(
                    new JwtTokenFilter(jwtTokenProvider, resolver), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
