// Separando responsabilidades em diferentes classes

package com.jonathan.springrestapiapp.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jonathan.springrestapiapp.enums.StatusPost;
import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;
import com.jonathan.springrestapiapp.exception.UnauthorizedUpdateException;
import com.jonathan.springrestapiapp.model.Image;
import com.jonathan.springrestapiapp.model.Log;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.repository.PostRepository;
import com.jonathan.springrestapiapp.rest.dto.PostDTO;
import com.jonathan.springrestapiapp.rest.dto.PostDescPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.PostUsersPatchDTO;
import com.jonathan.springrestapiapp.service.ImageService;
import com.jonathan.springrestapiapp.service.LogService;
import com.jonathan.springrestapiapp.service.PostService;
import com.jonathan.springrestapiapp.service.ProfileService;
import com.jonathan.springrestapiapp.service.Utils;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private Utils utils;

    

    @Autowired
    private LogService logService;

    @Override
    public Post save(PostDTO postDTO, MultipartFile file, String token) {
        Usuario usuario = utils.getUsuarioByToken(token);
        List<Profile> profiles = getProfilesFromDTO(postDTO, usuario);
        String imageUrl = utils.saveImage(file);
        Post post = buildPostFromDTO(postDTO, profiles, usuario, imageUrl);

        logService.save(new Log(usuario, "PostServiceImpl.save", "Novo post: " + post.getId()));

        return postRepository.save(post);
    }

    @Override
    public PostDTO getById(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> //se nao achar lança o erro!
                new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND,
                        "Publicação não encontrado"));
        return utils.converPostToDTO(post);
    }

    @Override
    public Post updatePostDesc(PostDescPatchDTO postDescPatchDTO, String token) {
        Post post = getPostById(postDescPatchDTO.getId());
        utils.validateAdmin(post, token);
        post.setDescricao(postDescPatchDTO.getDescricao());
        return postRepository.save(post);
    }

    @Override
    public Post updateUsersPost(PostUsersPatchDTO postUsersPatchDTO, String token) {
        Post post = getPostById(postUsersPatchDTO.getId());
        utils.validateAdmin(post, token);
        List<Profile> profiles = getProfilesByIds(postUsersPatchDTO.getUsers());
        post.setProfile(profiles);
        return postRepository.save(post);
    }

    @Override
    public PostDTO deletePost(Integer id, String token) {
        Post post = getPostById(id);
        utils.validateAdmin(post, token);
        post.setStatus(StatusPost.DELETADO);
        postRepository.save(post);
        logService.save(new Log(utils.getUsuarioByToken(token), "PostServiceImpl.deletePost", "Post deletado: " + id));
        return utils.converPostToDTO(post);
    }

    @Override
    public Post updatePost(PostDTO postDTO, MultipartFile file, String token) {
        Post post = getPostById(postDTO.getId());
        utils.validateAdmin(post, token);

        List<Profile> profiles = getProfilesFromDTO(postDTO, post.getAdmin());
        String imageUrl = file != null ? utils.saveImage(file) : post.getImg();

        post.setDescricao(postDTO.getDescricao());
        post.setGithub(postDTO.getGithub());
        post.setImg(imageUrl);
        post.setProfile(profiles);
        post.setStatus(postDTO.getStatus());
        post.setTitulo(postDTO.getTitulo());

        return postRepository.save(post);
    }

    @Override
    public List<PostDTO> getAllPost(String token) {
        Usuario usuario = utils.getUsuarioByToken(token);
        List<Post> posts = postRepository.findPostsByProfileId(usuario.getPerson().getProfile().getId());
        return utils.convertPostsToDTOs(posts);
    }

    private List<Profile> getProfilesFromDTO(PostDTO postDTO, Usuario admin) {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profileService.getClienteById(admin.getId()));
        if (postDTO.getProfile_ids() != null) {
            for (Integer id : postDTO.getProfile_ids()) {
                Profile profile = profileService.getClienteById(id);
                if (profile != null && !profiles.contains(profile)) {
                    profiles.add(profile);
                }
            }
        }
        return profiles;
    }

    private Post buildPostFromDTO(PostDTO postDTO, List<Profile> profiles, Usuario admin, String imageUrl) {
        return Post.builder()
                .descricao(postDTO.getDescricao())
                .github(postDTO.getGithub())
                .img(imageUrl)
                .status(postDTO.getStatus())
                .titulo(postDTO.getTitulo())
                .profile(profiles)
                .admin(admin)
                .build();
    }

    private Post getPostById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> //se nao achar lança o erro!
                new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND,
                        "Publicação não encontrado"));
    }

    private List<Profile> getProfilesByIds(List<Integer> profileIds) {
        List<Profile> profiles = new ArrayList<>();
        for (Integer id : profileIds) {
            profiles.add(profileService.getClienteById(id));
        }
        return profiles;
    }
}
