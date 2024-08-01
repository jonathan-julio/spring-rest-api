package com.jonathan.springrestapiapp.rest.dto;

import com.jonathan.springrestapiapp.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String login;
    private String token;
    private UserRole role;
}
