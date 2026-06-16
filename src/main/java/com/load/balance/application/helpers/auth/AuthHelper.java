package com.load.balance.application.helpers.auth;

import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import com.load.balance.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthHelper {

    private final UserRepository usersRepository;

    public AuthenticatedUser getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        assert auth != null;

        return (AuthenticatedUser) auth.getPrincipal();
    }

    public UUID getAuthenticatedUserId() {
        return this.getAuthenticatedUser().getId();
    }

    public Users getSessionUser() {
        return usersRepository
                .findById(this.getAuthenticatedUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}
