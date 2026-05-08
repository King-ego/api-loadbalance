package com.load.balance.application.usecase.users;

import com.load.balance.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckUsernameExistsUseCase {
    private UserRepository userRepository;
    public CheckUsernameExistsUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String username) {
        this.userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.info("User with username {} not found", username);
                    return new RuntimeException("User not found");
                });
    }
}
