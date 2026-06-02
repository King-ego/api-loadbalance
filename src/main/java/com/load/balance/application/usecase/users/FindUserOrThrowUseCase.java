package com.load.balance.application.usecase.users;

import com.load.balance.application.exceptions.users.UserNotFoundException;
import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class FindUserOrThrowUseCase {
    private final UserRepository userRepository;
    private static final Logger log =  LoggerFactory.getLogger(FindUserOrThrowUseCase.class);

    public FindUserOrThrowUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users byId(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User by id not found");
                    return new UserNotFoundException();
                });
    }

    public Users byEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User by email not found");
                    return new UserNotFoundException();
                });
    }
}
