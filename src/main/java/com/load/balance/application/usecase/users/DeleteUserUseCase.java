package com.load.balance.application.usecase.users;

import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeleteUserUseCase {
    private final UserRepository userRepository;
    private final FindUserOrThrowUseCase findUserOrThrowUseCase;
    private static final Logger log = LoggerFactory.getLogger(DeleteUserUseCase.class);

    public DeleteUserUseCase(UserRepository userRepository, FindUserOrThrowUseCase findUserOrThrowUseCase) {
        this.userRepository = userRepository;
        this.findUserOrThrowUseCase = findUserOrThrowUseCase;
    }

    public void execute(UUID userId) {
        Users user = findUserOrThrowUseCase.byId(userId);
        userRepository.delete(user);
        log.info("User deleted: {}", userId);
    }
}
