package com.load.balance.application.returns.users;

import com.load.balance.models.Users;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record SingleUser(
    UUID id,
    String username,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) implements Serializable {
    public static  SingleUser from(Users users) {
        return new SingleUser(
                users.getId(),
                users.getUsername(),
                LocalDateTime.now(),
                LocalDateTime.now());
    }
}
