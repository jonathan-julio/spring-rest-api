
package com.jonathan.springrestapiapp.rest.controllers;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.rest.dto.PostDTO;
import com.jonathan.springrestapiapp.rest.dto.PostDescPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.PostUsersPatchDTO;
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
    public Post save( @RequestBody @Valid PostDTO post, HttpServletRequest request){
        String token =  getToken(request);
        return postService.save(post,token);
    }

    //READ
    @GetMapping("{id}")
    public Post getById( @PathVariable @Valid Integer id ){
        return postService.getById(id);
    }

    @GetMapping("posts")
    public List<Post> getAllPostUser( HttpServletRequest request){
        String token =  getToken(request);
        return postService.getAllPost(token);
    }

    //DELETE
    @DeleteMapping("delete={id}")
    public Post deleteById(@PathVariable @Valid Integer id, HttpServletRequest request){
        String token =  getToken(request);
        return postService.deletePost(id, token);
    }

    //UPDATE
    @PutMapping("edit")
    public Post updateById( @RequestBody @Valid PostDTO post, HttpServletRequest request){
        String token =  getToken(request);
        return postService.updatePost(post, token);
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
