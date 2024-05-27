package com.jonathan.springrestapiapp.model;

import com.jonathan.springrestapiapp.rest.dto.UserDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Data
@Entity
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person") //opicional caso a tabela tenha nome igual da entidadeou caso use schema!

public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;
    @NotEmpty(message = "{campo.data.obrigatorio}")
    private String data;
    private String sexo;

    @OneToOne
    private Usuario usuario;

    @OneToOne(mappedBy = "person",cascade = CascadeType.ALL)
    private Profile profile;

    @PrePersist
    private void prePersist() {
        if (profile != null) {
            profile.setPerson(this);
        }
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public Profile getProfile(){
        return profile;
    }

    public void setProfile(Profile profile){
        this.profile = profile;
    }
    

    /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String nome;

    @Column(length = 11)
    private String cpf; */

    //um cliente pode ter muitos pedidos
    /*
     * mappedBy --> para a partir da classe cliente eu possa mapear/retornar os pedidos
     * passo como parametro o campo, na tabela de pedidos, que faz referencia a cliente.
     * no caso é cliente.
     * 
     * fetch  --> Lazy ou Eager - CUIDADO!!!
     */
    /* @JsonIgnore
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private Set<Post> posts; */

    
    
    
}
