package org.smms.authorization.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.smms.authorization.dto.UserDto;
import org.smms.authorization.entity.UserEntity;

/**
 * Mapper для {@link UserEntity} n {@link UserDto}
 */

 @Mapper(componentModel = "spring")
public interface UserMapper {
    
    /**
     * @param user {@link UserEntity}
     * @return {@link UserDto}
     */
     UserDto toDto(UserEntity user);

     /**
      * @param userDto {@link UserDto}
      * @return {@link UserEntity}
      */
      @Mapping(target = "id", ignore = true)
     UserEntity toEntity(UserDto userDto);

     /**
      * @param спикок пользователей {@link UserEntity}
      * @return список {@link UserDto} 
      */
     List<UserDto> toDtoList(List<UserEntity> userEntityList);

     /**
      * @param userDto {@link UserDto}
      * @param user {@link UserEntity}
      * @return {@link UserEntity}
      */
     @Mapping(target ="id", ignore = true)
     UserEntity mergeToEntity(UserDto userDto, @MappingTarget UserEntity user);
}
