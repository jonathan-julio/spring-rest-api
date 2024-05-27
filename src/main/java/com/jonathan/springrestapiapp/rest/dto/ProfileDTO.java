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

import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;

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
public class ProfileDTO {
    private String texto;
    private String textoSecundario;
    private String about;
    private String color;
    private String background;
}
