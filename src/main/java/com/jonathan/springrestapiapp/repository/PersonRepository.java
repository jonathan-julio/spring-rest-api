package com.jonathan.springrestapiapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jonathan.springrestapiapp.model.Person;

import jakarta.transaction.Transactional;

//classe e tipo do id
//start jpa já escaneia interfaces que herdam o JpaRepository - não precisa da anotaçao
public interface PersonRepository extends JpaRepository<Person,Integer>{


    
    @Query(value = " select * from person where usuario_id = :id ",nativeQuery = true)
    Optional<Person> findByUserId( @Param("id") int id );
    /*
     * Já tem um entity manager encapsulado que vai fazer as operações de transação!
     */

    /*
     * implementando um Query Method - que será executado com o entitymanager encapsulado
     */

    //selec c from Cliente c where c.nome like :nome
   /*  List<Person> findByNomeLike(String nome);
    List<Person> findByNomeOrId(String nome, Integer id);
    boolean existsByNome(String nome); */

    /*
     * Trabalhando com @Query
     */
    //hql
   /*  @Query(value = " select c from Cliente c where c.nome like %:nome% ")
    List<Person> encontrarPorNome(@Param("nome") String nome);

    //sql nativo
    @Query(value = " select * from cliente c where c.nome like %:nome% ",nativeQuery = true)
    List<Person> encontrarPorNomeMod(@Param("nome") String nome);

    @Query(value = " delete from Cliente c where c.nome =:nome")
    @Modifying //pois não é só consulta - transactional 
    void deletarPorNome(String nome);

    @Query(" select c from Cliente c left join fetch c.pedidos where c.id = :id  ")
    Person findClienteFetchPedidos( @Param("id") Integer id );

    @Modifying //pois não é só consulta - transactional 
    @Transactional
    @Query(value = " UPDATE Cliente c SET c.nome = :nome WHERE c.id = :id ")
    int editarNome(Integer id, String nome); */

    
    
}
