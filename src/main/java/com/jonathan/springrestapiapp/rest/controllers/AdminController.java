package com.jonathan.springrestapiapp.rest.controllers;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.jonathan.springrestapiapp.enums.AcessoUsuario;
import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.MyException;
import com.jonathan.springrestapiapp.model.Log;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.AcessoDTO;
import com.jonathan.springrestapiapp.rest.dto.LogDTO;
import com.jonathan.springrestapiapp.rest.dto.PapelDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchAdmDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.TokenDTO;
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
    public ResponseEntity<Object> getBloqueados() {
        try {
            return ResponseEntity
                    .ok(usuarioService.getAllBloqueados());
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }

    @GetMapping("/logs/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getLogs(@PathVariable @Valid Integer id) {

        try {
            return ResponseEntity
                    .ok(usuarioService.getLogsById(id));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }

    @PostMapping("/alterar-papel")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> alterarPapel(@RequestBody @Valid PapelDTO tipo, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);

        try {
            return ResponseEntity
                    .ok(usuarioService.alterarPapel(tipo, token));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }

    }

    @PostMapping("/acesso-usuario")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> alterarAcesso(@RequestBody @Valid AcessoDTO acesso) {
        try {
            return ResponseEntity
                    .ok(usuarioService.alterarAcesso(acesso));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }

    }

    @PatchMapping("/patch-password")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> patchPassword(@RequestBody @Valid SenhaPatchAdmDTO DTO) {

        try {
            return ResponseEntity
                    .ok(usuarioService.patchPasswordAdmin(DTO));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }

}
