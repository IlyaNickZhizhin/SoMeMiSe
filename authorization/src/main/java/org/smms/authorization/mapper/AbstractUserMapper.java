 package org.smms.authorization.mapper;

 import org.mapstruct.Mapper;
 import org.mapstruct.Mapping;
 import org.smms.authorization.dto.ProfileDto;
 import org.smms.authorization.feign.ProfileClient;
 import org.springframework.beans.factory.annotation.Autowired;

 import lombok.extern.slf4j.Slf4j;

 /**
  * Абстрактный класс для маппинга {@link org.smms.authorization.entity.UserEntity} в {@link ProfileDto}
  */
 @Mapper(componentModel = "spring")
 @Slf4j
 public abstract class AbstractUserMapper {

     @Autowired
     ProfileClient profileClient;

     /**
      * @param id идентификатор {@link org.smms.authorization.entity.UserEntity}
      * @return profile {@link ProfileDto}
      */
     @Mapping(target = "id", source = "id")
     public ProfileDto toProfileDto(Long id) {
         log.info("Снаряжаем фейнг клиент профиль по ID {}", id);
         return profileClient.getProfileById(id);
     }
 }
