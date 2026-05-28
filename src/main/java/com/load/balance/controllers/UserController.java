package com.load.balance.controllers;

import com.load.balance.application.dtos.users.CreateUserDto;
import com.load.balance.application.returns.users.SingleUser;
import com.load.balance.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServices userService;

    public UserController(UserServices userService) {
        this.userService = userService;
    }

    @PostMapping
    public void createUser(@RequestBody CreateUserDto createUserDto) {
        this.userService.createUser(createUserDto);
    }

    @GetMapping("/username")
    public SingleUser getUserName() {
        return this.userService.getUserByUsername("TED");
    }

    @GetMapping
    public List<SingleUser> getUsers() {
        return this.userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        this.userService.deleteUser(id);
    }
}
