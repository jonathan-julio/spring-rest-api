package com.jonathan.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.jonathan.springrestapiapp.enums.StatusPost;
import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;

/*
 * {
    "cliente" : 1,
    "total" : 100,
    "items" : [
        {
            "produto": 1,
            "quantidade": 10
        }
        
    ]
}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDTO {
    private String nome;
    private String data;
    private String sexo;
    private ProfileDTO profile;
}
