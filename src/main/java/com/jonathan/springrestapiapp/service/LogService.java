package com.jonathan.springrestapiapp.service;

import java.util.List;

import com.jonathan.springrestapiapp.model.Log;



public interface LogService {

     public void save(Log log) ;
     public List<Log> getAllByUsuario(Integer id) ;
     
}
