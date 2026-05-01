package com.load.balance.services;

import com.load.balance.application.dtos.auth.CreateLoginDefault;
import com.load.balance.application.returns.users.SingleUser;
import com.load.balance.models.Users;
import com.load.balance.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(AuthServices.class);

    public AuthServices(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public SingleUser authenticateDefault(CreateLoginDefault loginDefault, HttpSession session) {
        Users user = userRepository.findByEmail(loginDefault.getEmail());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(loginDefault.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }


        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        List.of(new SimpleGrantedAuthority("MEMBER"))
                );

        SecurityContextImpl securityContext = new SecurityContextImpl(authentication);
        SecurityContextHolder.setContext(securityContext);

        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );
        session.setAttribute("userId", user.getId().toString());
        session.setAttribute("userRole", "MEMBER");
        session.setMaxInactiveInterval(1800);


        log.info("User authenticated: {}", user.getUsername());
        return SingleUser.from(user);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
