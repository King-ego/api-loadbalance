package com.load.balance.services;

import com.load.balance.application.dtos.users.CreateUserDto;
import com.load.balance.application.returns.users.SingleUser;
import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserServices.class);

    public UserServices(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional(readOnly = false)
    public void createUser(CreateUserDto createUserDto) {
        Users user = Users.builder()
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .email(createUserDto.getEmail())
                .build();

        this.userRepository.save(user);
        log.info("User created: {}", createUserDto.getUsername());
    }

    @Cacheable(value = "users", key = "#username")
    @Transactional(readOnly = true)
    public SingleUser getUserByUsername(String username) {
        log.info("Fetching user: {}", username);
        Users user = this.userRepository.findByUsername(username);
        return SingleUser.from(user);
    }

    @Cacheable(value = "users", key = "'allUsers'")
    @Transactional(readOnly = true)
    public List<SingleUser> getAllUsers() {
        log.info("Fetching all users");
        List<Users> users = this.userRepository.findAll();

        return users.stream()
                .map(SingleUser::from)
                .toList();
    }
}