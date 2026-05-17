package com.load.balance.application.exceptions.auth;

import com.load.balance.application.exceptions.base.CustomException;
import org.springframework.http.HttpStatus;

public class PasswordNotMatch extends CustomException{
    public PasswordNotMatch() {
        super(HttpStatus.NOT_ACCEPTABLE, "Passwords do not match", "PASSWORD_NOT_MATCH");
    }
}
