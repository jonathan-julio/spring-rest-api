package com.jonathan.springrestapiapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.jonathan.springrestapiapp.model.Profile;



public interface ProfileRepository extends JpaRepository<Profile,Integer>{

   /*  List<Post> findByCliente(Person cliente);
    //Optional<Pedido> findByIdFetchItens(Integer id);
    @Query(" select p from Pedido p left join fetch p.itens where p.id = :id ")
    Optional<Post> findByIdFetchItens(@Param("id") Integer id); */
}
