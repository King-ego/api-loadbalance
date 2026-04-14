package com.load.balance.controllers;

import com.load.balance.models.Users;
import com.load.balance.services.UserServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServices userService;

    public UserController(UserServices userService) {
        this.userService = userService;
    }

    @PostMapping
    public void createUser() {
        String username = "TED";
        this.userService.createUser(username);
    }

    @GetMapping

    public Users getUserName() {
        return this.userService.getUserByUsername("TED");
    }
}
