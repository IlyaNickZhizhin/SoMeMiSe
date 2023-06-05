package org.smms.authorization.repository;

import java.util.Optional;

import org.smms.authorization.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для {@link UserEntity}
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);    
}
