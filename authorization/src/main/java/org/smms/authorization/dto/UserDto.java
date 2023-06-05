package org.smms.authorization.dto;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.smms.authorization.entity.UserEntity;

/**
 * DTO для {@link UserEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String login;
    String password;
    String role;
    Long profileId;
}
