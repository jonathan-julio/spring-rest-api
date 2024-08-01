package com.jonathan.springrestapiapp.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Profile;



public interface ProfileRepository extends JpaRepository<Profile,Integer>{


    @Query(value = " select * from profile where usuario_id = :id ",nativeQuery = true)
    Optional<Profile> findByUserId( @Param("id") int id );

   /*  List<Post> findByCliente(Person cliente);
    //Optional<Pedido> findByIdFetchItens(Integer id);
    @Query(" select p from Pedido p left join fetch p.itens where p.id = :id ")
    Optional<Post> findByIdFetchItens(@Param("id") Integer id); */
}
