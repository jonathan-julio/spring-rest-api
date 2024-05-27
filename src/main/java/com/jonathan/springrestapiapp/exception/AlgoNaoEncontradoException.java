package com.jonathan.springrestapiapp.exception;

public class AlgoNaoEncontradoException extends RuntimeException {

    public AlgoNaoEncontradoException(String string) {
        super(string);
    }
    public AlgoNaoEncontradoException() {
        super("Post not found");
    }
}
