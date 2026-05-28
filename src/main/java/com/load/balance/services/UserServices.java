package com.load.balance.services;

import com.load.balance.application.dtos.users.CreateUserDto;
import com.load.balance.application.returns.users.SingleUser;
import com.load.balance.application.usecase.users.CheckEmailUseCase;
import com.load.balance.application.usecase.users.CheckUsernameExistsUseCase;
import com.load.balance.application.usecase.users.DeleteUserUseCase;
import com.load.balance.application.usecase.users.FindUserOrThrowUseCase;
import com.load.balance.application.usecase.users.ValidatePasswordUseCase;
import com.load.balance.enums.UserRoles;
import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CheckEmailUseCase checkEmailUseCase;
    private final CheckUsernameExistsUseCase checkUsernameExistsUseCase;
    private final ValidatePasswordUseCase validatePasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final FindUserOrThrowUseCase findUserOrThrowUseCase;

    public UserServices(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CheckUsernameExistsUseCase checkUsernameExistsUseCase,
            CheckEmailUseCase checkEmailUseCase,
            ValidatePasswordUseCase validatePasswordUseCase,
            DeleteUserUseCase deleteUserUseCase,
            FindUserOrThrowUseCase findUserOrThrowUseCase
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.checkUsernameExistsUseCase = checkUsernameExistsUseCase;
        this.checkEmailUseCase = checkEmailUseCase;
        this.validatePasswordUseCase = validatePasswordUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.findUserOrThrowUseCase = findUserOrThrowUseCase;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional()
    public void createUser(CreateUserDto createUserDto) {
        this.checkEmailUseCase.notExist(createUserDto.getEmail());
        this.checkUsernameExistsUseCase.notExist(createUserDto.getUsername());
        this.validatePasswordUseCase.execute(createUserDto.getPassword(), createUserDto.getConfirmPassword());

        Users user = Users.builder()
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .email(createUserDto.getEmail())
                .role(UserRoles.MEMBER)
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

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void deleteUser(UUID userId) {
        Users user = this.findUserOrThrowUseCase.byId(userId);
        this.deleteUserUseCase.execute(user);
        log.info("User deleted: {}", userId);
    }
}
