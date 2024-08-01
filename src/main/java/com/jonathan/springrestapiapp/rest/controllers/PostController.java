
package com.jonathan.springrestapiapp.rest.controllers;


import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.MyException;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.PostDTO;
import com.jonathan.springrestapiapp.rest.dto.PostDescPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.PostUsersPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.TokenDTO;
import com.jonathan.springrestapiapp.security.JwtService;
import com.jonathan.springrestapiapp.service.PostService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
@SecurityRequirement(name = "Bearer Authentication")
public class PostController {

    @Autowired
    JwtService jwt;

    @Autowired
    PostService postService;

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> save( PostDTO post, @RequestPart("file") MultipartFile file, HttpServletRequest request){
        String token =  getToken(request);
        try {
            return ResponseEntity.status(201).body(postService.save(post, file, token));
        } catch ( MyException e) {
            return ResponseEntity.status(e.getCode())
                    .body(Collections.singletonMap("errors", List.of("Erro na criação da postagem: " + e.getMessage())));
        }
       
    }

    //READ
    @GetMapping("{id}")
    public PostDTO getById( @PathVariable @Valid Integer id ){
        return postService.getById(id);
    }

    @GetMapping("posts")
    public List<PostDTO> getAllPostUser( HttpServletRequest request){
        String token =  getToken(request);
        return postService.getAllPost(token);
    }

    //DELETE
    @DeleteMapping("delete={id}")
    public PostDTO deleteById(@PathVariable @Valid Integer id, HttpServletRequest request){
        String token =  getToken(request);
        return postService.deletePost(id, token);
    }

    //UPDATE
    @PutMapping("edit")
    public Post updateById( PostDTO post, MultipartFile file, HttpServletRequest request) {
        String token =  getToken(request);
        return postService.updatePost(post, file, token);
       
    }

    //PATCH
    @PatchMapping("patch-users")
    public Post pathUsersById(@RequestBody @Valid PostUsersPatchDTO dto, HttpServletRequest request){
        String token =  getToken(request);
        return postService.updateUsersPost(dto, token);
    }
     //PATH
     @PatchMapping("patch-descricao")
     public Post patchDescricao(@RequestBody @Valid PostDescPatchDTO dto, HttpServletRequest request){
        String token =  getToken(request);
         return postService.updatePostDesc(dto, token);
     }

     private String getToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
     }
    
}
