package com.jonathan.springrestapiapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.rest.dto.PostDTO;
import com.jonathan.springrestapiapp.rest.dto.PostDescPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.PostUsersPatchDTO;

public interface PostService {

    public Post save(PostDTO post, MultipartFile file, String token);

    public PostDTO getById(Integer id);

    public List<PostDTO> getAllPost(String token);

    public Post updatePost(PostDTO DTO, MultipartFile file, String token);

    public Post updatePostDesc(PostDescPatchDTO DTO, String token);

    public Post updateUsersPost(PostUsersPatchDTO DTO, String token);

    public PostDTO deletePost(Integer ID, String token);

}
