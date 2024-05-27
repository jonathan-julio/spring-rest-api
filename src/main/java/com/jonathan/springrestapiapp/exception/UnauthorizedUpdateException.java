package com.jonathan.springrestapiapp.exception;

public class UnauthorizedUpdateException extends RuntimeException {
    public UnauthorizedUpdateException() {
        super("Not authorized to update this post");
    }

    public UnauthorizedUpdateException(String message) {
        super(message);
    }
}
