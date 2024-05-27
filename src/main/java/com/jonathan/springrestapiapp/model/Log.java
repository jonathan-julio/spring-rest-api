package com.jonathan.springrestapiapp.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Data
@Entity
@Table(name = "log")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    private String function;
    private String changer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data;


    

    public Log() {
    }

    public Log(Usuario usuario, String function, String changer) {
        this.usuario = usuario;
        this.function = function;
        this.changer = changer;
        this.data = new Date(); 
    }

    public Log(Integer id, Usuario usuario, String function, String changer, Date data) {
        this.id = id;
        this.usuario = usuario;
        this.function = function;
        this.changer = changer;
        this.data = data;
    }

    
}