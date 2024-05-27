package com.jonathan.springrestapiapp.enums;

public enum AcessoUsuario {
    LIBERADO("LIBERADO"),
    BLOQUEADO("BLOQUEADO");

    private String role;

    AcessoUsuario(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
