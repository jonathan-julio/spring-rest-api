package com.jonathan.springrestapiapp.rest.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.jonathan.springrestapiapp.exception.SenhaInvalidaException;
import com.jonathan.springrestapiapp.exception.UnauthorizedUpdateException;
import com.jonathan.springrestapiapp.exception.UsuarioBloqueadoException;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.CredenciaisDTO;
import com.jonathan.springrestapiapp.rest.dto.TokenDTO;
import com.jonathan.springrestapiapp.rest.dto.UserCreaterDTO;
import com.jonathan.springrestapiapp.rest.dto.UserDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchDTO;
import com.jonathan.springrestapiapp.security.JwtService;
import com.jonathan.springrestapiapp.service.impl.UsuarioServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;




@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar( @RequestBody @Valid UserCreaterDTO usuario ){
        return usuarioService.created(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody @Valid CredenciaisDTO credenciais){
        try{
            UserDetails usuarioAutenticado = usuarioService.autenticar(credenciais);
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuarioAutenticado.getUsername(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException | UsuarioBloqueadoException e ){
            throw new UnauthorizedUpdateException(e.getMessage());
        }
    }

    @PatchMapping("/patch-password")
    @SecurityRequirement(name = "Bearer Authentication")
    public UserDTO patchPassword(@RequestBody @Valid SenhaPatchDTO DTO,HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        return usuarioService.patchPassaword(DTO,token);
       
    }

    
    @GetMapping("{id}")
    public UserDTO getClienteById( @PathVariable @Valid Integer id){
        return usuarioService.findById(id);
                
    }

  
}
