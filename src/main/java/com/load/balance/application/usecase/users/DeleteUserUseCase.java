package com.load.balance.application.usecase.users;

import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserUseCase {
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(DeleteUserUseCase.class);

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Users user) {
        userRepository.delete(user);
        log.info("User deleted: {}", user.getId());
    }
}