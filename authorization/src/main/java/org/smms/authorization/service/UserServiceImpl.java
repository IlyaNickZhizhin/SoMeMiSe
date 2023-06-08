package org.smms.authorization.service;

import java.util.List;

import org.smms.authorization.dto.UserDto;
import org.smms.authorization.entity.UserEntity;
// import org.smms.authorization.mapper.AbstractUserMapper;
import org.smms.authorization.mapper.UserMapper;
import org.smms.authorization.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "Пользователь с логином %s не найден";

    private final UserMapper mapper;
    // private final AbstractUserMapper abstractMapper;
    private final UserRepository repository;

    public UserServiceImpl(UserMapper mapper, UserRepository repository 
            //, AbstractUserMapper abstractMapper
    ) {
        this.mapper = mapper;
        this.repository = repository;
        // this.abstractMapper = abstractMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, login)));
    }

    @Override
    public Long findByLogin(String login) {
        return repository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, login)))
            .getId();
    }

    @Override
    public UserDto findById(Long id) {
        final UserEntity user = repository.findById(id)
                .orElseThrow(() -> getEntityNotFoundException(id));
        
        final UserDto userDto = mapper.toDto(user);
        // userDto.setProfile(abstractMapper.toProfileDto(user.getProfileId()));
        return userDto;
    }

    @Override
    @Transactional
    public UserDto save(@Valid UserDto user) {
 
        final UserEntity userEntity = mapper.toEntity(user);

        userEntity.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        final UserEntity savedUser = repository.save(userEntity);

        final UserDto savedUserDto = mapper.toDto(savedUser);

        // savedUserDto.setProfile(abstractMapper.toProfileDto(savedUser.getProfileId()));

        return savedUserDto;
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto user) {

        final UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> getEntityNotFoundException(id));

        if (user.getPassword() != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        
        final UserEntity updatedUser = repository.save(mapper.mergeToEntity(user, userEntity));
        final UserDto mergeDto = mapper.toDto(updatedUser);
        // mergeDto.setProfile(abstractMapper.toProfileDto(updatedUser.getProfileId()));
        
        return mergeDto;
    }

    @Override
    public List<UserDto> findAllByIds(List<Long> ids) {

        final List<UserEntity> users = ids.stream()
                .map(id -> repository.findById(id)
                .orElseThrow(
                    () -> getEntityNotFoundException(id))
                    )
                    .toList();

        return mapper.toDtoList(users);
    }

    @Override
    @Transactional
    public UserDto deleteById(Long id) {
        final UserEntity user4del = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        repository.delete(user4del);
        return mapper.toDto(user4del);
    }

    private EntityNotFoundException getEntityNotFoundException(Long id) {
        return new EntityNotFoundException(String.format(USER_NOT_FOUND_MSG, id));
    }
    
}
