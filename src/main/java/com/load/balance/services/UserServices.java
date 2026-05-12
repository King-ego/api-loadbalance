package com.load.balance.services;

import com.load.balance.application.dtos.users.CreateUserDto;
import com.load.balance.application.returns.users.SingleUser;
import com.load.balance.application.usecase.users.CheckEmailExistsUseCase;
import com.load.balance.application.usecase.users.CheckUsernameExistsUseCase;
import com.load.balance.application.usecase.users.ValidatePasswordUseCase;
import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CheckEmailExistsUseCase checkEmailExistsUseCase;
    private final CheckUsernameExistsUseCase checkUsernameExistsUseCase;
    private final ValidatePasswordUseCase validatePasswordUseCase;

    public UserServices(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CheckUsernameExistsUseCase checkUsernameExistsUseCase,
            CheckEmailExistsUseCase checkEmailExistsUseCase,
            ValidatePasswordUseCase validatePasswordUseCase
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.checkUsernameExistsUseCase = checkUsernameExistsUseCase;
        this.checkEmailExistsUseCase = checkEmailExistsUseCase;
        this.validatePasswordUseCase = validatePasswordUseCase;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional()
    public void createUser(CreateUserDto createUserDto) {
        this.checkEmailExistsUseCase.notExist(createUserDto.getEmail());
        this.checkUsernameExistsUseCase.notExist(createUserDto.getUsername());
        this.validatePasswordUseCase.execute(createUserDto.getPassword(), createUserDto.getConfirmPassword());

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
        Users user = this.userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );
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
