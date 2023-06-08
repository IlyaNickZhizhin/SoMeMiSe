package org.smms.authorization.service;

import java.util.List;

import org.smms.authorization.dto.UserDto;

import org.springframework.stereotype.Service;

/**
 * Ceрвис для {@link UserDto} и {@link UserEntity}
 */

@Service
public interface UserService {

    /**
     * @param id идентификатор {@link UserEntity}
     * @return {@link UserDto}
     */
    UserDto findById(Long id);

    /**
     * @param user {@link UserDto}
     * @return {@link UserDto}
     */
    UserDto save(UserDto user);

    /**
     * @param id идентификатор {@link UserEntity}
     * @param user {@link UserDto}
     * @return {@link UserDto}
     */
    UserDto update(Long id, UserDto user);

    /**
     * @param ids лист идентификаторов {@link UserEntity}
     * @return List<{@link UserDto}>
     */
    List<UserDto> findAllByIds(List<Long> ids);


    /**
     * @param id идентификатор {@link UserEntity}
     * @return {@link UserDto} удаленного пользователя
     */
    UserDto deleteById(Long id);

    Long findByLogin(String login);
}
