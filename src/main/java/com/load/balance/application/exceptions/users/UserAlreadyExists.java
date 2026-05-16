package com.load.balance.application.exceptions.users;

import com.load.balance.application.exceptions.base.CustomException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExists extends CustomException {
    public UserAlreadyExists() {
        super(HttpStatus.CONFLICT, "User already exists", "User_Already_Exists");

    }
}
