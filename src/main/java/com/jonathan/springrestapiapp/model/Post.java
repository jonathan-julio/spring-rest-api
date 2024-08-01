package com.jonathan.springrestapiapp.model;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jonathan.springrestapiapp.enums.StatusPost;
import com.jonathan.springrestapiapp.rest.dto.UsuarioDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Usuario admin;
    private String img;
    @NotEmpty(message = "{campo.titulo.obrigatorio}")
    @Column(length=100)
    private String titulo;
    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    @Column(length=100000)
    private String descricao;
    private String github;
    private StatusPost status;

    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "profile_post",
        joinColumns = @JoinColumn(name = "post_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private List<Profile> profile;

    
    
    
}
