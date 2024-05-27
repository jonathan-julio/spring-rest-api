package com.jonathan.springrestapiapp.rest.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDescPatchDTO {
    private Integer id;
    private String descricao;

}
