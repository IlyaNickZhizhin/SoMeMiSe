package org.smms.profile.controller;

import java.util.List;

import org.smms.profile.dto.ProfileDto;
import org.smms.profile.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Контроллер для работы {@link ProfileEntity}
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Контроллер для работы с профилями")
public class ProfileController {
    
    private final ProfileService service;

    /**
     * @param profile содержит данные профиля {@link ProfileDto}
     * @return {@link ProfileDto} и статус {@link HttpStatus}.CREATED
     */
    @PostMapping("/registration")
    @Operation(summary = "Регистрация профиля")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProfileDto> registration(@RequestBody ProfileDto profile) {
        log.info("Регистрируем пользователя {}", profile.getName());
        return ResponseEntity.ok(service.save(profile));
    }

    /**
     * @param id идентификатор {@link ProfileEntity}
     * @return {@link ProfileDto} и статус {@link HttpStatus}.OK
     */
    @Operation(summary = "Получение профиля по ID")
    @GetMapping("read/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProfileDto> read(@PathVariable Long id) {
        log.info("Получаем профиль по ID {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * @param id идентификатор {@link ProfileEntity}
     * @param profile содержит данные профиля {@link ProfileDto}
     * @return {@link ProfileDto} и статус {@link HttpStatus}.OK
     */
    @Operation(summary = "Обновление профиля")
    @PutMapping("update/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProfileDto> update(@PathVariable Long id, @RequestBody ProfileDto profile) {
        return ResponseEntity.ok(service.update(id, profile));
    }

    /**
     * @param ids список идентификаторов {@link ProfileEntity}
     * @return список {@link ProfileDto} и статус {@link HttpStatus}.OK
     */
    @Operation(summary = "Получение списка профилей по списку ID")
    @GetMapping("read/all")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ProfileDto>> readAllByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.findAllByIds(ids));
    }

    /**
     * @param followId идентификатор подписчика {@link ProfileEntity}
     * @param followingId идентификатор подписки {@link ProfileEntity}
     * @return {@link ProfileDto} подписчика и статус {@link HttpStatus}.OK
     */
    @Operation(summary = "Подписка на профиль")
    @PostMapping("follow/{followId}/{followingId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProfileDto> follow(@PathVariable Long followId, @PathVariable Long followingId) {
        return ResponseEntity.ok(service.follow(followId, followingId));
    }

    /**
     * @param unfollowId идентификатор отписчика {@link ProfileEntity}
     * @param unfollowingId идентификатор отписки {@link ProfileEntity}
     * @return {@link ProfileDto} отписчика и статус {@link HttpStatus}.OK
     */
    @Operation(summary = "Отписка от профиля")
    @PostMapping("unfollow/{unfollowId}/{unfollowingId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProfileDto> unfollow(@PathVariable Long unfollowId, @PathVariable Long unfollowingId) {
        return ResponseEntity.ok(service.unfollow(unfollowId, unfollowingId));
    }

    /**
     * @param id идентификатор {@link ProfileEntity}
     * @return {@link ProfileDto} и статус {@link HttpStatus}.OK
     */
    @Operation(summary = "Удаление профиля")
    @DeleteMapping("delete/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProfileDto> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

}
