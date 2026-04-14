package com.load.balance.services;

import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServices {
    private final UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = false)
    public void createUser(String name) {
        Users user = Users.builder()
                .username(name)
                .password("Ted")
                .build();

        this.userRepository.save(user);
        System.out.println("Creating user: " + name);
    }

    @Transactional(readOnly = true)
    public void getUserByUsername(String username) {
        System.out.println("Fetching user by username: " + username);
         // Placeholder for actual user retrieval logic
    }
}
