package com.jonathan.springrestapiapp.rest.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class UserCreaterDTO {
    
    private String login;
    private String email;
    private String senha;
    private PersonDTO person;
   
   
}

