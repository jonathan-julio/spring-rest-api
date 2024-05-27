package com.jonathan.springrestapiapp.enums;


public enum UserRole {
    ADMIN("admin"),
    MODERADOR("moderador"),
    USER("user"),
    ESPECIAL("especial"); // futuramente tera acesso a outras funcionalidades

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

}
