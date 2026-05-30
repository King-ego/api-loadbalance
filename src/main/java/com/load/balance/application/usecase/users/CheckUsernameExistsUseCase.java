package com.load.balance.application.usecase.users;

import com.load.balance.application.exceptions.users.UserAlreadyExists;
import com.load.balance.application.exceptions.users.UserNotFoundException;
import com.load.balance.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckUsernameExistsUseCase {
    private final UserRepository userRepository;
    public CheckUsernameExistsUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void exist(String username) {
        this.userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.info("User with username {} not found", username);
                    return new UserNotFoundException();
                });
    }

    public void notExist(String username) {
        this.userRepository.findByUsername(username).ifPresent(user -> {;
            log.info("User with username {} already exists", username);
            throw new UserAlreadyExists();
        });
    }
}

    