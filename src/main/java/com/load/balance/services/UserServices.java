package com.load.balance.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServices {
    @Transactional(readOnly = false)
    public void createUser(String name) {
        System.out.println("Creating user: " + name);
    }

    @Transactional(readOnly = true)
    public void getUserByUsername(String username) {
        System.out.println("Fetching user by username: " + username);
         // Placeholder for actual user retrieval logic
    }
}
