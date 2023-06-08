package org.smms.profile.repository;

import org.smms.profile.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с профилями
 */
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
    
}
