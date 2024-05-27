package com.jonathan.springrestapiapp.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Post;

import jakarta.transaction.Transactional;


public interface PostRepository extends JpaRepository<Post,Integer>{

  @Query("SELECT p FROM Post p JOIN p.profile pr WHERE pr.id = :id")
  List<Post> findPostsByProfileId(@Param("id") Integer id);
    
  /*   @Modifying //pois não é só consulta - transactional 
    @Transactional
    @Query(value = " UPDATE Produto c SET c.preco = :preco WHERE c.id = :id ")
    int editarPreco(Integer id, BigDecimal preco); */
}
