package com.nevits.exception;

import com.nevits.error.ErrorCodeEnum;
import com.nevits.error.InternalServerErrorException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends InternalServerErrorException {
    public InvalidCredentialsException() {
        super(new ServerErrorResponseDto("Invalid username or password", ErrorCodeEnum.INVALID_USER_CREDENTIALS,
                HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
    }
}
