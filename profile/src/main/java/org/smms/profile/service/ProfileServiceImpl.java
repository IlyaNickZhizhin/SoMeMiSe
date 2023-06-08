package org.smms.profile.service;

import java.util.List;

import org.smms.profile.dto.ProfileDto;
import org.smms.profile.entity.ProfileEntity;
import org.smms.profile.mapper.ProfileMapper;
import org.smms.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final static String PROFILE_NOT_FOUND = "Не был найден профиль с ID:";
    private final ProfileMapper mapper;
    private final ProfileRepository repository;


    @Override
    public ProfileDto findById(Long id) {
        final ProfileEntity profile = repository.findById(id)
                .orElseThrow(() -> getEntityNotFoundException(id));

        final ProfileDto profileDto = mapper.toDto(profile);
        return profileDto;
    }

    @Override
    @Transactional
    public ProfileDto save(ProfileDto profileDto) {
        log.info("Регистрируем пользователя {}", profileDto);
        final ProfileEntity profile = mapper.toEntity(profileDto);
        log.info("Мапер отработал", profile.getName());
        final ProfileEntity savedProfile = repository.save(profile);
        log.info("Сохранение выполнено", profile.getName());
        return mapper.toDto(savedProfile);
    }

    @Override
    @Transactional
    public ProfileDto update(Long id, ProfileDto changingDto) {
        final ProfileEntity oldProfileEntity = repository.findById(id)
                .orElseThrow(() -> getEntityNotFoundException(id));

        final ProfileEntity newProfileEntity = mapper.mergeToEntity(changingDto, oldProfileEntity);
        final ProfileEntity savedProfileEntity = repository.save(newProfileEntity);
        return mapper.toDto(savedProfileEntity);
    }

    @Override
    public List<ProfileDto> findAllByIds(List<Long> ids) {
        final List<ProfileEntity> profiles = repository.findAllById(ids);
        return mapper.toDtoList(profiles);
    }

    @Override
    @Transactional
    public ProfileDto deleteById(Long id) {
        final ProfileEntity profile = repository.findById(id)
                .orElseThrow(() -> getEntityNotFoundException(id));

        repository.deleteById(id);
        return mapper.toDto(profile);
    }

    @Override
    @Transactional
    public ProfileDto follow(Long followId, Long followingId) {
        final ProfileEntity follower = repository.findById(followId)
                .orElseThrow(() -> getEntityNotFoundException(followId));
        final ProfileEntity following = repository.findById(followingId)
                .orElseThrow(() -> getEntityNotFoundException(followingId));
        follower.getFollowing().add(following);
        following.getFollowers().add(follower);
        repository.save(follower);
        repository.save(following);
        return mapper.toDto(follower);
    }

    @Override
    @Transactional
    public ProfileDto unfollow(Long unfollowId, Long unfollowingId) {
        final ProfileEntity unfollower = repository.findById(unfollowId)
                .orElseThrow(() -> getEntityNotFoundException(unfollowId));
        final ProfileEntity unfollowing = repository.findById(unfollowingId)
                .orElseThrow(() -> getEntityNotFoundException(unfollowingId));
        unfollower.getFollowing().remove(unfollowing);
        unfollowing.getFollowers().remove(unfollower);
        repository.save(unfollower);
        repository.save(unfollowing);
        return mapper.toDto(unfollower);
    }

    private EntityNotFoundException getEntityNotFoundException(Long id) {
        return new EntityNotFoundException(PROFILE_NOT_FOUND + id);
    }
    
}
