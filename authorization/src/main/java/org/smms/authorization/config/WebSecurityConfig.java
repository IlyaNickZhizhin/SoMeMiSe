package org.smms.authorization.config;

import jakarta.servlet.http.HttpServletRequest;

import org.smms.authorization.validator.Roles;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    
    private static final String LOGIN_ENDPOINT = "/login";
    private static final String ADMIN_ENDPOINT = "/read/**";
    private static final String[] AUTH_WHITELIST = {
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private final JwtTokenProvider jwtTokenProvider;
    private final HandlerExceptionResolver resolver;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider, 
                            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.resolver = resolver;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder, 
    UserDetailsService userDetailsService) throws Exception {
        
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and() //TODO заменить с использованием лямбда выражения
            .build();      
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic((htp) -> htp.disable())
            .csrf(cr ->
                    cr.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((request) -> request
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/registration").permitAll()
                    //TODO после настройки инструментов раскоменировать
/*                  .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()*/
                    .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtTokenFilter(jwtTokenProvider, resolver), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
}
