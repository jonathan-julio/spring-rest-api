package com.jonathan.springrestapiapp.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super("Senha inválida");
    }
}
