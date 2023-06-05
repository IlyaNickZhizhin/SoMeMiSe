package org.smms.authorization.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smms.authorization.dto.UserDto;
import org.smms.authorization.entity.UserEntity;
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

@Service
public class UserServiceImpl implements UserService, UserDetailsService{

    private final static String USER_NOT_FOUND_MSG = "Пользователь с логином %s не найден";

    private final UserMapper mapper;
    private final UserRepository repository;
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserMapper mapper, UserRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
         return repository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, login)));
    }

    @Override
    public UserDto findById(Long id) {
        final UserEntity user = repository.findById(id)
            .orElseThrow(() -> getEntityNotFoundException(id));

        final UserDto userDto = mapper.toDto(user);

        return userDto;
    }

    @Override
    @Transactional
    public UserDto save(@Valid UserDto user) {

        final UserEntity userEntity = mapper.toEntity(user);
        logger.info("Пользователь {} успешно зарегистрирован", user.getUsername());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        logger.info("Пароль пользователя {} успешно зашифрован", user.getUsername());
        final UserEntity savedUser = repository.save(userEntity);
        logger.info("Пользователь {} успешно сохранен в базе данных", savedUser.getUsername());
        final UserDto savedUserDto = mapper.toDto(savedUser);
        logger.info("Пользователь {} успешно сохранен в базе данных", savedUserDto.getUsername());
        return savedUserDto;
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto user) {

        final UserEntity userEntity = repository.findById(id)
            .orElseThrow(() -> getEntityNotFoundException(id));

        final UserEntity updatedUser = repository.save(mapper.mergeToEntity(user, userEntity));
        final UserDto mergeDto = mapper.toDto(updatedUser);
        
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
    public UserDto deleteById(Long id){
        final UserEntity user4del = repository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
        repository.delete(user4del);
        return mapper.toDto(user4del);
    }

    private EntityNotFoundException getEntityNotFoundException(Long id) {
        return new EntityNotFoundException(String.format(USER_NOT_FOUND_MSG, id));
    }
    
}
