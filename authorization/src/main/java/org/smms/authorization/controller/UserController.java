package org.smms.authorization.controller;
/**
 * Контроллер для {@link UserDto}
 */

import java.util.List;

import org.smms.authorization.dto.UserDto;
import org.smms.authorization.entity.UserEntity;
import org.smms.authorization.service.AuthService;
import org.smms.authorization.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@Tag(name = "User", description = "Контроллер для работы с пользователями")
public class UserController {

    private final UserServiceImpl userService;
    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
   
    public UserController(UserServiceImpl userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * @param user содержит логин и пароль, введенные при аутентификации {@link UserDto}
     * @return {@link ResponseEntity} содержит token {@link String} и статус {@link HttpStatus}.CREATED 
     */
    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<String> loginToken(@RequestBody UserDto user) {
        logger.info("Получен запрос на логирование пользователя: {}", user.getUsername());
        final String token = authService.login(user);
        logger.info("Пользователь {} успешно авторизован и получен токен {}", user.getUsername(), token);
        

        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    /**
     * @param user содержит логин и пароль, при регистрации для дальнешего доступа {@link UserDto}
     * @return {@link ResponseEntity} содержит {@link UserDto} и статус {@link HttpStatus}.CREATED
     */
    @PostMapping("/registration")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<UserDto> registration(@RequestBody UserDto user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    /**
     * @param id идентификатор {@link UserEntity}
     * @return {@link ResponseEntity} содержит {@link UserDto} и статус {@link HttpStatus}.OK
     */
    @GetMapping("/read/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasAuthority('ADMIN')")
    @Operation(summary = "Получение пользователя по id", description = "Метод получения пользователя по id")
    public ResponseEntity<UserDto> read(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("authentication.principal.id == #id")
    @Operation(summary = "Обновление пользователя по id", description = "Метод обновления данных пользователя по id")
    public ResponseEntity<UserDto> update(@PathVariable("id") Long id,
                                            @RequestBody UserDto user) {
        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
    }

    /**
     * @param ids лист идентификаторов {@link UserEntity}
     * @return {@link ResponseEntity} содержит List<{@link UserDto}> и статус {@link HttpStatus}.OK
     */
    @GetMapping("/read/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Получение списка пользователей по списку ID",
            description = "Метод получения списка пользователей")
    public ResponseEntity<List<UserDto>> readAll(@RequestParam("ids") List<Long> ids) {
        return new ResponseEntity<>(userService.findAllByIds(ids), HttpStatus.OK);
    }

    /**
     * @param id идентификатор {@link UserEntity}
     * @return {@link ResponseEntity} содержит {@link UserDto} и статус {@link HttpStatus}.OK
     */
    @GetMapping("/delete/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasAuthority('ADMIN')")
    @Operation(summary = "Удаление пользователя по id", description = "Метод удаления пользователя по id")
    public ResponseEntity<UserDto> deleteById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.deleteById(id), HttpStatus.OK);
    }
}
