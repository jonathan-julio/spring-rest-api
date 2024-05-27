package com.jonathan.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.jonathan.springrestapiapp.enums.StatusPost;

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
public class PostDTO {
    private Integer id;
    private StatusPost status;
    private String descricao;
    private String github;
    private String img;
    private String Titulo;
    private List<Integer> profile_ids;
}
