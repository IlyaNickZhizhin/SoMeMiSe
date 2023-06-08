package org.smms.profile.service;

import java.util.List;
import org.smms.profile.dto.ProfileDto;

/**
 * Сервис для {@link ProfileDto} и {@link ProfileEntity}
 */
public interface ProfileService {
    
    /**
     * @param id идентификатор {@link ProfileEntity}
     * @return {@link ProfileDto}
     */
    ProfileDto findById(Long id);

    /**
     * @param profileDto {@link ProfileDto}
     * @return {@link ProfileDto}
     */ 
    ProfileDto save(ProfileDto profileDto);

    /**
     * @param id идентификатор {@link ProfileEntity}
     * @param profile {@link ProfileDto}
     * @return {@link ProfileDto}
     */
    ProfileDto update(Long id, ProfileDto profile);

    /**
     * @param ids список идентификаторов {@link ProfileEntity}
     * @return список {@link ProfileDto}
     */
    List<ProfileDto> findAllByIds(List<Long> ids);

    /**
     * @param followId идентификатор {@link ProfileEntity}
     * @param followingId идентификатор {@link ProfileEntity}
     * @return {@link ProfileDto}
     */
    ProfileDto follow(Long followId, Long followingId);

    /**
     * @param followId идентификатор {@link ProfileEntity}
     * @param followingId идентификатор {@link ProfileEntity}
     * @return {@link ProfileDto}
     */
    ProfileDto unfollow(Long unfollowId, Long unfollowingId);

    /**
     * @param id идентификатор {@link ProfileEntity}
     * @return {@link ProfileDto}
     */
    ProfileDto deleteById(Long id);

}
