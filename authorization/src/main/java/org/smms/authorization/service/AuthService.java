package org.smms.authorization.service;

import org.smms.authorization.config.JwtTokenProvider;
import org.smms.authorization.dto.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserServiceImpl userService;

    /**
     * @param user содежит введенные пользователем данные {@link UserDto}
     * @return token, сгенерированный для пользователя
     */
    public String login(UserDto user) {       
        final String login = user.getLogin();
        final Long userId = userService.findByLogin(login);
        final Long profileId = userService.findById(userId).getProfile().getId();
        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login, user.getPassword())
        );
        
        return jwtTokenProvider.createToken(login, authentication.getAuthorities().toString(), profileId);
    } 

}
