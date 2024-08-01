package com.jonathan.springrestapiapp.exception;

import org.springframework.http.HttpStatus;



public class SenhaInvalidaException extends MyException {

    public SenhaInvalidaException(HttpStatus code,String message) {
        super( code ,message);
    }
}
