package com.jonathan.springrestapiapp.service;

import java.util.List;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.rest.dto.PostDTO;
import com.jonathan.springrestapiapp.rest.dto.PostDescPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.PostUsersPatchDTO;



public interface PostService {

    public Post save(PostDTO post, String token);
    public Post getById(Integer id );
    public List<Post> getAllPost(String token );
    public Post updatePost(PostDTO DTO, String token);
    public Post updatePostDesc( PostDescPatchDTO DTO, String token);
    public Post updateUsersPost( PostUsersPatchDTO DTO, String token);
    public Post deletePost(Integer ID, String token);

}
