package org.smms.authorization.service;

import org.smms.authorization.config.JwtTokenProvider;
import org.smms.authorization.dto.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    /**
     * @param user содежит введенные пользователем данные {@link UserDto}
     * @return token, сгенерированный для пользователя
     */
    public String login(UserDto user) {
          
        final String login = user.getUsername();
        logger.info("Запрос пользователя: {} в сервисе", login);
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login, user.getPassword())
        );
        
        return jwtTokenProvider.createToken(login.toString(), authentication.getAuthorities().toString());
    } 

}
