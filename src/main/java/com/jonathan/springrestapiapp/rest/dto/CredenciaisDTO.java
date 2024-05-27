package com.jonathan.springrestapiapp.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDTO {
    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String login;
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;
}
