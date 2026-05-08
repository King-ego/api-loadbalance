package com.load.balance.application.usecase.users;

import com.load.balance.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckEmailExistsUseCase {
    private final UserRepository userRepository;

    public CheckEmailExistsUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String email) {
        this.userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.info("User with email {} not found", email);
                    return new RuntimeException("User not found");
                });
    }
}
