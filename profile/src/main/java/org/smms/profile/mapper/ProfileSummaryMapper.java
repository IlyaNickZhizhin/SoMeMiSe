package org.smms.profile.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.smms.profile.dto.ProfileDto;
import org.smms.profile.dto.ProfileSummaryDto;
import org.smms.profile.entity.ProfileEntity;

import jakarta.inject.Named;

/**
 * Mapper для {@link ProfileEntity} n {@link ProfileSummaryDto}
 */
@Mapper(componentModel = "spring")
public interface ProfileSummaryMapper {
    
    /**
     * Обрезает поля, которые не нужны для {@link ProfileSummaryDto}
     * @param profileDto {@link ProfileDto}
     * @return {@link ProfileSummaryDto}
     */
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    ProfileSummaryDto toDto(ProfileDto profileDto);

    /**
     * Преобразует списки друзей из Set<{@link ProfileEntity}> в Set<{@link ProfileSummaryDto}>
     * это нужно, для корретной работы {@link ProfileMapper#toDto(ProfileEntity)}
     * @param profileSummaryDto {@link ProfileSummaryDto}
     * @return {@link ProfileDto}
     */
    @Named("toSummaryDto")
    Set<ProfileSummaryDto> toSummaryDtoSet(Set<ProfileEntity> profileEntitySet);


    /**
     * Преобразует списки друзей из Set<{@link ProfileSummaryDto}> в Set<{@link ProfileEntity}>
     * при этом передает только номера, а не все поля
     * это нужно, для сохранения БД в случае изменения подписок
     * @param profileSummaryDtos
     * @return
     */
    @Named("toEntitySet")
    @Mapping(source = "id", target = "id")
    Set<ProfileEntity> toEntitySet(Set<ProfileSummaryDto> profileSummaryDtos);
}


