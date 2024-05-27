package com.jonathan.springrestapiapp.rest.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCreaterDTO {
    
    private String login;
    private String senha;
    private PersonDTO person;
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public PersonDTO getPerson() {
        return person;
    }
    public void setPerson(PersonDTO person) {
        this.person = person;
    }
   
}

