package com.load.balance.controllers;

import com.load.balance.application.dtos.auth.CreateLoginDefault;
import com.load.balance.application.returns.users.SingleUser;
import com.load.balance.services.AuthServices;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServices authServices;

    public AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping("/login")
    public SingleUser login(@RequestBody CreateLoginDefault loginDefault, HttpSession session) {
        return authServices.authenticateDefault(loginDefault, session);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        authServices.logout(session);
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        if (userId == null) {
            throw new RuntimeException("Not authenticated");
        }

        return Map.of(
                "userId", userId,
                "role", userRole,
                "sessionId", session.getId()
        );
    }
}
