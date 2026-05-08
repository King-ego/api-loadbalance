package com.load.balance.application.usecase.users;

import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class FindUserOrThrowUseCase {
    private final UserRepository userRepository;

    public FindUserOrThrowUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users execute(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
