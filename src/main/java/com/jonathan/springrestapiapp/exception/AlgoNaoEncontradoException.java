package com.jonathan.springrestapiapp.exception;

import org.springframework.http.HttpStatus;

public class AlgoNaoEncontradoException extends MyException {
    public AlgoNaoEncontradoException(HttpStatus code, String message) {
        super(code, message);
    }

}