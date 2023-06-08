package org.smms.profile.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.smms.profile.dto.ProfileDto;
import org.smms.profile.entity.ProfileEntity;

/**
 * Mapper для {@link ProfileEntity} n {@link ProfileDto}
 */
@Mapper(componentModel = "spring", uses = {ProfileSummaryMapper.class})
public interface ProfileMapper {

    ProfileDto toDto(ProfileEntity profile);

    ProfileEntity toEntity(ProfileDto profileDto);

    List<ProfileDto> toDtoList(List<ProfileEntity> profileEntityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "bio", target = "bio")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    ProfileEntity mergeToEntity(ProfileDto profileDto, @MappingTarget ProfileEntity profile);
}


