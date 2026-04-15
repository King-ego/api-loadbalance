package com.load.balance.services;

import com.load.balance.application.returns.users.SingleUser;
import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServices {
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserServices.class);

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
        log.info("User created: {}", name);
    }

    @Transactional(readOnly = true)
    public SingleUser getUserByUsername(String username) {
        log.info("Fetching user: {}", username);
        Users user = this.userRepository.findByUsername(username);
        return SingleUser.from(user);
    }
}
