package com.jonathan.springrestapiapp.exception;

import org.springframework.http.HttpStatus;

public abstract class MyException extends RuntimeException {
    private HttpStatus code;
    private String msg;

    public MyException() {
    }

    public MyException(HttpStatus code, String msg) {
        super(msg); // Armazena a mensagem no RuntimeException
        this.code = code;
        this.msg = msg;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
