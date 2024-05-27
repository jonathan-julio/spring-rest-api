package com.jonathan.springrestapiapp.model;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jonathan.springrestapiapp.enums.AcessoUsuario;
import com.jonathan.springrestapiapp.enums.UserRole;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.regex.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;



import java.util.regex.Matcher;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String login;
    @Column
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;
    @Column
    private UserRole role;
    @Column
    private AcessoUsuario acesso;

    @OneToOne(mappedBy = "usuario",cascade = CascadeType.ALL)
    private Person person;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Log> logs;

    @PrePersist
    public void prePersist(){
        if (person != null) {
            person.setUsuario(this);
            if (person.getProfile() != null) {
                person.getProfile().setPerson(person);
            }
        }
    }
    
/* 
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of(new SimpleGrantedAuthority(this.role.getRole()));
    } */


}
