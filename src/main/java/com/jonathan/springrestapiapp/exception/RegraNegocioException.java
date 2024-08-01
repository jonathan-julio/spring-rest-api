package com.jonathan.springrestapiapp.exception;

import org.springframework.http.HttpStatus;



public class RegraNegocioException extends MyException {
    private String message;

    public RegraNegocioException(HttpStatus code, String message) {
        super(code,  message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
