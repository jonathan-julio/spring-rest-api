package com.jonathan.springrestapiapp.service.impl;

import java.beans.JavaBean;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jonathan.springrestapiapp.model.Log;
import com.jonathan.springrestapiapp.repository.LogRepository;
import com.jonathan.springrestapiapp.service.LogService;

@Component
public class LogServiceImpl implements LogService {

    @Autowired
    LogRepository logRepository;


    @Override
    public void save(Log log) {
        logRepository.save(log);
    }

    @Override
    public List<Log> getAllByUsuario(Integer id) {
        List<Log> findAll = logRepository.findAllByUserId(id);

        return findAll;
    }

    

    
    
}