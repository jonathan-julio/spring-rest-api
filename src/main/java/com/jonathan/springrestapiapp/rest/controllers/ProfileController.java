package com.jonathan.springrestapiapp.rest.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.springrestapiapp.exception.MyException;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.ProfileDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchDTO;
import com.jonathan.springrestapiapp.service.ProfileService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;




@RequestMapping("/api/profile")
@SecurityRequirement(name = "Bearer Authentication")
@RestController //anotação especializadas de controller - todos já anotados com response body!
public class ProfileController {


    @Autowired
    ProfileService profileService;

    

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profile save( @RequestBody @Valid Profile profile ){
        return profileService.save(profile);
    }


    @GetMapping("{id}")
    public Profile getClienteById( @PathVariable @Valid Integer id ){
        return profileService
                .getClienteById(id);
    }

    //UPDATE
    @PutMapping("edit")
    public ProfileDTO updateById(@RequestBody @Valid ProfileDTO updatedProfile, HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        return profileService
                .updatProfile(updatedProfile, token);
    }

    @PatchMapping("/alterar-html/{isHtml}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> patchPassword(@PathVariable Boolean isHtml, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            return ResponseEntity.ok(profileService.patchHtml(isHtml, token));
        } catch (MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of(e.getMessage())));
        }
    }
}
