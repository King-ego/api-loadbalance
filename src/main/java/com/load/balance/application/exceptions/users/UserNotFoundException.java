package com.load.balance.application.exceptions.users;

import com.load.balance.application.exceptions.base.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found", "USER_NOT_FOUND");
    }
}
