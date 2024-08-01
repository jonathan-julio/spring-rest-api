package com.jonathan.springrestapiapp.rest.controllers;

import java.awt.event.FocusEvent.Cause;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.MyException;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;
import com.jonathan.springrestapiapp.exception.SenhaInvalidaException;
import com.jonathan.springrestapiapp.exception.UnauthorizedUpdateException;
import com.jonathan.springrestapiapp.exception.UsuarioBloqueadoException;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.CredenciaisDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPostDTO;
import com.jonathan.springrestapiapp.rest.dto.TokenDTO;
import com.jonathan.springrestapiapp.rest.dto.UserCreaterDTO;
import com.jonathan.springrestapiapp.rest.dto.UserDTO;
import com.jonathan.springrestapiapp.rest.dto.UsuarioDTO;
import com.jonathan.springrestapiapp.security.JwtService;
import com.jonathan.springrestapiapp.service.impl.UsuarioServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;

    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody UserCreaterDTO usuario) {
        try {
            Usuario createdUsuario = usuarioService.created(usuario);
            return ResponseEntity.ok(createdUsuario);
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of("Erro na criação do usuário: " + e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<Object> autenticar(@RequestBody @Valid CredenciaisDTO credenciais) {
        try {
            UserDetails usuarioAutenticado = usuarioService.autenticar(credenciais);
            Usuario usuario = usuarioService.findByLogin(usuarioAutenticado.getUsername())
                    .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
            String token = jwtService.gerarToken(usuario);
            return ResponseEntity
                    .ok(new TokenDTO(usuarioAutenticado.getUsername(), token, usuario.getRole()));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }

    @PostMapping("/check-token")
    public ResponseEntity<Object> checkTokenDTO(@Valid HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        System.out.println(token);
        String login = jwtService.obterLoginUsuario(token);
        Usuario usuario = usuarioService.findByLogin(login)
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        if (jwtService.tokenValido(token)) {
            return ResponseEntity.ok(new TokenDTO(login, token, usuario.getRole()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("errors", List.of("Token invalido")));
        }
    }

    @PatchMapping("/patch-password")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> patchPassword(@RequestBody @Valid SenhaPatchDTO DTO, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            return ResponseEntity.ok(usuarioService.patchPassword(DTO, token));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }

    @PostMapping("/recover-password/token={token}")
    public ResponseEntity<Object> recoverPassword(@RequestBody SenhaPostDTO DTO, @PathVariable String token) {

        try {
            return ResponseEntity.ok(usuarioService.recoverPassword(DTO, token));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getClienteById(@PathVariable @Valid Integer id) {

        try {
            return ResponseEntity.ok(usuarioService.findById(id));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }

    }

    @GetMapping("login={name}")
    public ResponseEntity<Object> getClienteById(@PathVariable @Valid String name) {
        try {
            return ResponseEntity.ok(usuarioService.findDtoByLogin(name));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }

    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllUser() {

        try {
            return ResponseEntity.ok(usuarioService.getAll());
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }

}
