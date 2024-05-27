package com.jonathan.springrestapiapp.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jonathan.springrestapiapp.enums.AcessoUsuario;
import com.jonathan.springrestapiapp.enums.UserRole;
import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.UnauthorizedUpdateException;
import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.repository.UsuarioRepository;
import com.jonathan.springrestapiapp.rest.dto.PersonDTO;
import com.jonathan.springrestapiapp.rest.dto.ProfileDTO;
import com.jonathan.springrestapiapp.rest.dto.UserCreaterDTO;
import com.jonathan.springrestapiapp.rest.dto.UserDTO;
import com.jonathan.springrestapiapp.security.JwtService;
import com.jonathan.springrestapiapp.service.impl.UsuarioServiceImpl;


@Service
public class Utils {

    @Autowired
    public LogService logService;

    @Autowired
    public JwtService jwt;

    @Autowired
    public UsuarioRepository usuarioRepository;


    public Usuario converteUsuarioDTO(UserCreaterDTO dto) {
        Usuario usuario = Usuario.builder()
        .acesso(AcessoUsuario.LIBERADO)
        .role(UserRole.USER)
        .login(dto.getLogin())
        .senha(dto.getSenha())
        .person(convertePersonDTO(dto.getPerson()))
        .build();
        return usuario;
    }

    public Person convertePersonDTO(PersonDTO dto) {
        Person person = Person.builder()
        .nome(dto.getNome())
        .data(dto.getData())
        .sexo(dto.getSexo())
        .profile(converteProfileDTO(dto.getProfile() ))
        .build();
        return person;
    }

    

    public Profile converteProfileDTO(ProfileDTO dto) {
        Profile profile = Profile.builder()
        .background(dto.getBackground())
        .about(dto.getAbout())
        .color(dto.getColor())
        .texto(dto.getTexto())
        .textoSecundario(dto.getTextoSecundario())
        .build();
        return profile;
    }

    public UserDTO converteUsuarioToDTO(Usuario usuario) {
        UserDTO dto = UserDTO.builder()
        .login(usuario.getLogin())
        .admin(usuario.getRole())
        .person(convertePersonToDTO(usuario.getPerson()))
        .posts(usuario.getPosts())
        .build();
        return dto;
    }


    public PersonDTO convertePersonToDTO(Person person) {
        PersonDTO dto = PersonDTO.builder()
        .nome(person.getNome())
        .data(person.getData())
        .sexo(person.getSexo())
        .profile(converteProfileToDTO(person.getProfile()))
        .build();
        return dto;
    }

    public ProfileDTO converteProfileToDTO(Profile profile) {
        ProfileDTO dto = ProfileDTO.builder()
        .background(profile.getBackground())
        .about(profile.getAbout())
        .color(profile.getColor())
        .texto(profile.getTexto())
        .textoSecundario(profile.getTextoSecundario())
        .build();
        return dto;
    }

    public Usuario getUsuarioByToken(String token){
        String usuarioString = jwt.obterLoginUsuario(token);
        Usuario usuario = usuarioRepository.findByLogin(usuarioString).orElseThrow(() -> new AlgoNaoEncontradoException("Usuário não encontrado com o token fornecido"));
        return usuario;
    }

    public boolean isValid(String input) {
        String regex = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    @SuppressWarnings("unlikely-arg-type")
    public void validateAdmin(Post post, String token) {
        Usuario usuario = getUsuarioByToken(token);
        if (!post.getAdmin().equals(usuario)) {
            throw new UnauthorizedUpdateException("Not authorized to update this post");
        }
    }
}
