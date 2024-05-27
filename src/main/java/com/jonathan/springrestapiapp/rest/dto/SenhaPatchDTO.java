package com.jonathan.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.jonathan.springrestapiapp.enums.StatusPost;
import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;

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
public class SenhaPatchDTO {
    private String password;
}
