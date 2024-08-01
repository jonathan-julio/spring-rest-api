package com.jonathan.springrestapiapp.rest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.MyException;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;
import com.jonathan.springrestapiapp.exception.UnauthorizedUpdateException;
import com.jonathan.springrestapiapp.rest.ApiErrors;



@RestControllerAdvice //trata os exceptionHandlers --> trata erros quando elesa acontecem
public class ApplicationControllerAdvice {

    /* 
     * toda vez que api lançar essa exceção, cairá aqui!
     * preciso dizer qual status será retornado - por padrão -bad request - 400
    */

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ApiErrors> handleMyException(MyException ex) {
        ApiErrors apiErrors = new ApiErrors(ex.getMsg());
        return new ResponseEntity<>(apiErrors, ex.getCode());
    }
    
    @ExceptionHandler(RegraNegocioException.class)
    public ApiErrors handleRegraNegocioException(RegraNegocioException ex){
        return new ApiErrors(ex.getMessage());
    }
    

    @ExceptionHandler(AlgoNaoEncontradoException.class)
    public ApiErrors handlePedidoNotFoundException( AlgoNaoEncontradoException ex ){
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrors handleMethodNotValidException( MethodArgumentNotValidException ex ){
        List<String> errors = ex.getBindingResult().getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(errors);
    }
    @ExceptionHandler(UnauthorizedUpdateException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrors handleUnauthorizedException( UnauthorizedUpdateException ex ){
        return new ApiErrors(ex.getMessage());
    }
}
