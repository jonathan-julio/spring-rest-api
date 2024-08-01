package com.jonathan.springrestapiapp.exception;

import org.springframework.http.HttpStatus;

public class UsuarioBloqueadoException extends MyException {


    public UsuarioBloqueadoException(HttpStatus code,String message) {
        super(code, message);
    }
}
