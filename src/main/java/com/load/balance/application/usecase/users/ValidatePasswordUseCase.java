package com.load.balance.application.usecase.users;

import com.load.balance.application.exceptions.auth.PasswordNotMatch;
import org.springframework.stereotype.Component;

@Component
public class ValidatePasswordUseCase {
    public ValidatePasswordUseCase() {}

    public void execute(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordNotMatch();
        }
    }
}
