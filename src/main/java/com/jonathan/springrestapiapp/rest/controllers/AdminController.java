package com.jonathan.springrestapiapp.rest.controllers;


import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.jonathan.springrestapiapp.enums.AcessoUsuario;
import com.jonathan.springrestapiapp.model.Log;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.LogDTO;
import com.jonathan.springrestapiapp.rest.dto.PapelDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.UserCreaterDTO;
import com.jonathan.springrestapiapp.rest.dto.UserDTO;
import com.jonathan.springrestapiapp.service.impl.UsuarioServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;




@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final UsuarioServiceImpl usuarioService;


    @GetMapping("/bloqueados")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<UserDTO> getBloqueados(){
        return usuarioService.getAllBloqueados();
    }

    @GetMapping("/logs/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<LogDTO> getLogs(@PathVariable @Valid Integer id){
        return usuarioService.getLogsById(id);
    }

    @PostMapping("/alterar-papel")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDTO alterarPapel(@RequestBody @Valid PapelDTO  tipo, HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        return usuarioService.alterarPapel(tipo,token);
       
    }
    
    @PostMapping("/acesso-usuario")
    @SecurityRequirement(name = "Bearer Authentication")
    public Usuario alterarAcesso(@RequestBody @Valid AcessoUsuario acesso,HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        return usuarioService.alterarAcesso(acesso,token);
       
    }

    @PatchMapping("/patch-password/id={id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDTO patchPassword(@RequestBody @Valid SenhaPatchDTO DTO,Integer id){
        return usuarioService.patchPassaword(DTO,id);
    }

  
}
