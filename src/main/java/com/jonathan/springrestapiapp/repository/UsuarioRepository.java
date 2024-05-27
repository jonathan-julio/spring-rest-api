package com.jonathan.springrestapiapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jonathan.springrestapiapp.model.Usuario;



public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByLogin(String login);

    @Query(value = "SELECT * FROM usuario WHERE acesso = :acesso", nativeQuery = true)
    List<Usuario> findAllByAcesso(@Param("acesso") int acesso);

    boolean existsByLogin(String login);
}
