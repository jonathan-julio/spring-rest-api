package com.jonathan.springrestapiapp.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedUpdateException extends MyException {

    public UnauthorizedUpdateException(HttpStatus code,String message) {
        super(code, message);
    }

}
