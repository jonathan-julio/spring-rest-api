package com.jonathan.springrestapiapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jonathan.springrestapiapp.model.Log;



public interface LogRepository extends JpaRepository<Log,Integer>{

    @Query(value = "SELECT * FROM log WHERE usuario_id = :id", nativeQuery = true)
    List<Log> findAllByUserId(@Param("id") Integer id);
    
}
