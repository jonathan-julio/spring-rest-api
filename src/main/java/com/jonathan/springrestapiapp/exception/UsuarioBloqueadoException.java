package com.jonathan.springrestapiapp.exception;

public class UsuarioBloqueadoException extends RuntimeException {
    public UsuarioBloqueadoException() {
        super("Usuario bloqueado.");
    }
}
