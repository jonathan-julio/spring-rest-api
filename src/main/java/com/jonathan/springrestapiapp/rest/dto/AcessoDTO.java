package com.jonathan.springrestapiapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.jonathan.springrestapiapp.enums.AcessoUsuario;
import com.jonathan.springrestapiapp.enums.UserRole;




@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AcessoDTO {
    private Integer usuarioID;
    private AcessoUsuario acesso;
}
