package com.jonathan.springrestapiapp.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jonathan.springrestapiapp.enums.StatusPost;
import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;
import com.jonathan.springrestapiapp.exception.UnauthorizedUpdateException;
import com.jonathan.springrestapiapp.model.Log;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.repository.PostRepository;
import com.jonathan.springrestapiapp.rest.dto.PostDTO;
import com.jonathan.springrestapiapp.rest.dto.PostDescPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.PostUsersPatchDTO;
import com.jonathan.springrestapiapp.security.JwtService;
import com.jonathan.springrestapiapp.service.LogService;
import com.jonathan.springrestapiapp.service.PostService;
import com.jonathan.springrestapiapp.service.ProfileService;
import com.jonathan.springrestapiapp.service.Utils;



@Component
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UsuarioServiceImpl userService;

    @Autowired
    ProfileService profileService;

    @Autowired
    Utils utils;

    @Override
    public Post save(PostDTO post, String token) {
        List<Profile> profiles = new ArrayList<>();

        Usuario usuario = utils.getUsuarioByToken(token);
        Profile usuarioProfile = profileService.getClienteById(usuario.getId());
        if (usuarioProfile != null) {
            profiles.add(usuarioProfile);
        }
        if (post.getProfile_ids() != null) {
            for (Integer id : post.getProfile_ids()) {
                Profile profile = profileService.getClienteById(id);
                if (profile != null && !profiles.contains(profile)) { 
                    profiles.add(profile);
                }
            }
        }
     
        Post _post = Post.builder()
            .descricao(post.getDescricao())
            .github(post.getGithub())
            .img(post.getImg())
            .profile(profiles)
            .status(post.getStatus()) 
            .titulo(post.getTitulo())
            .admin(usuario) 
            .build();

        Log log = new Log(usuario, "PostServiceImpl.save", "Novo post: " + _post.toString() );
        utils.logService.save(log);

        return postRepository.save(_post);
    }

    @Override
    public Post getById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() ->
                        new AlgoNaoEncontradoException());
    }

    
    public Post updatePostDesc(PostDescPatchDTO DTO, String token) {
        Post post = getPostById(DTO.getId());
        utils.validateAdmin(post, token);
        return updatePostDescription(post, DTO.getDescricao(), token);
    }

    public Post updateUsersPost(PostUsersPatchDTO DTO, String token) {
        Post post = getPostById(DTO.getId());
        utils.validateAdmin(post, token);
        List<Profile> users = getProfilesByIds(DTO.getUsers());
        return updatePostUsers(post, users, token);
    }

    @Override
    public Post deletePost(Integer ID, String token) {
        Post post = getPostById(ID);
        utils.validateAdmin(post, token);
        return deletePostEnum(post, StatusPost.DELETADO, token);
    }

    @Override
    public Post updatePost(PostDTO DTO, String token) {
        return postRepository.findById(DTO.getId()).map(_post -> {
            utils.validateAdmin(_post, token);
            List<Profile> profiles = profileService.findAllById(DTO.getProfile_ids());
            _post.setDescricao(DTO.getDescricao());
            _post.setGithub(DTO.getGithub());
            _post.setImg(DTO.getImg());
            _post.setProfile(profiles);
            _post.setStatus(DTO.getStatus());
            _post.setTitulo(DTO.getTitulo());
            Usuario usuario = utils.getUsuarioByToken(token);
            Log log = new Log(usuario, "PostServiceImpl.deletePostEnum", "Post editado para: " + _post.toString() );
            utils.logService.save(log);
            return postRepository.save(_post);
        }).orElseThrow(() -> new AlgoNaoEncontradoException());
            
    }

    private Post deletePostEnum(Post post, StatusPost status, String token) {
        return postRepository.findById(post.getId()).map(_post -> {
            _post.setStatus(status);
            Usuario usuario = utils.getUsuarioByToken(token);
            Log log = new Log(usuario, "PostServiceImpl.deletePostEnum", "Post editado para: " + _post.toString() );
            utils.logService.save(log);
            return postRepository.save(_post);
        }).orElseThrow(() -> new AlgoNaoEncontradoException());
    }


    private Post updatePostDescription(Post post, String descricao, String token) {
        return postRepository.findById(post.getId()).map(_post -> {
            Usuario usuario = utils.getUsuarioByToken(token);
            Log log = new Log(usuario, "PostServiceImpl.updatePostDescription", "Post editado para: " + _post.toString() );
            utils.logService.save(log);
            _post.setDescricao(descricao);
            return postRepository.save(_post);
        }).orElseThrow(() -> new AlgoNaoEncontradoException());
    }

    private Post getPostById(Integer id) {
        @SuppressWarnings("deprecation")
        Post post = postRepository.getById(id);
        return post;
    }
    

    private List<Profile> getProfilesByIds(List<Integer> userIds) {
        List<Profile> users = new ArrayList<>();
        for (Integer id : userIds) {
            users.add(profileService.getClienteById(id));
        }
        return users;
    }

    
    private Post updatePostUsers(Post post, List<Profile> users, String token ) {
        return postRepository.findById(post.getId()).map(_post -> {
            _post.setProfile(users);
            Usuario usuario = utils.getUsuarioByToken(token);
            Log log = new Log(usuario, "PostServiceImpl.updatePostUsers", "Post editado para: " + _post.toString() );
            utils.logService.save(log);
            return postRepository.save(_post);
        }).orElseThrow(() -> new AlgoNaoEncontradoException( ));
    }

    @Override
    public List<Post> getAllPost(String token) {
        Usuario usuario = utils.getUsuarioByToken(token);
        List<Post> posts = postRepository.findPostsByProfileId(usuario.getPerson().getProfile().getId());
        return posts;
    }
    
}