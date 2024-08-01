package com.jonathan.springrestapiapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.jonathan.springrestapiapp.enums.AcessoUsuario;
import com.jonathan.springrestapiapp.enums.UserRole;
import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.UnauthorizedUpdateException;
import com.jonathan.springrestapiapp.model.Image;
import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Post;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.repository.UsuarioRepository;
import com.jonathan.springrestapiapp.rest.dto.PersonDTO;
import com.jonathan.springrestapiapp.rest.dto.PostDTO;
import com.jonathan.springrestapiapp.rest.dto.ProfileDTO;
import com.jonathan.springrestapiapp.rest.dto.UserCreaterDTO;
import com.jonathan.springrestapiapp.rest.dto.UserDTO;
import com.jonathan.springrestapiapp.security.JwtService;
import com.jonathan.springrestapiapp.service.impl.UsuarioServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class Utils {

    @Autowired
    public LogService logService;

    @Autowired
    public JwtService jwt;

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    ImageService imageService;



    public Usuario converteUsuarioDTO(UserCreaterDTO dto) {
        Usuario usuario = Usuario.builder()
        .acesso(AcessoUsuario.LIBERADO)
        .role(UserRole.USER)
        .email(dto.getEmail()  )
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

    public String saveImage(MultipartFile file ){
        Image img = new Image();
        try {
            img.setImage(file.getBytes());
            return " https://80bc-177-89-225-229.ngrok-free.app/image/view/" + imageService.save(img).getId();
             
        } catch (IOException e) {
            e.printStackTrace();
            return " https://80bc-177-89-225-229.ngrok-free.app/image/view/1";
        }        
    }

    @SuppressWarnings("null")
    public String salveFile(MultipartFile file){
        if (file.isEmpty()) {
            return "Arquivo não enviado.";
        }

        try {
            // Defina o caminho onde deseja salvar o arquivo
            String uploadDir = "src/main/java/com/jonathan/springrestapiapp/public/";
            Path uploadPath = Paths.get(uploadDir);

            // Crie o diretório se não existir
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Gere um nome único para o arquivo
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Verifique se o arquivo é uma imagem
            if (!file.getContentType().startsWith("image")) {
                return "O arquivo enviado não é uma imagem.";
            }

            // Salve o arquivo no diretório especificado
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            return "http://localhost:8080/image/" + uniqueFileName;
        } catch (Exception e) {
            return "Falha ao enviar o arquivo: " + e.getMessage();
        }
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
        .id(usuario.getId())
        .email(usuario.getEmail())
        .login(usuario.getLogin())
        .role(usuario.getRole())
        .person(convertePersonToDTO(usuario.getPerson()))
        .posts(convertPostsToDTOs(usuario.getPosts()))
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
        .id(profile.getId())
        .background(profile.getBackground())
        .about(profile.getAbout())
        .color(profile.getColor())
        .texto(profile.getTexto())
        .textoSecundario(profile.getTextoSecundario())
        .build();
        return dto;
    }


    public List<Integer> convertProfList(List<Profile> profiles) {
        List<Integer> dto = new ArrayList<>();
        for (Profile Profile : profiles) {
            dto.add(Profile.getId());
        }
        return dto;
    }

    
    public PostDTO converPostToDTO( Post post){
        PostDTO dto = new PostDTO();
        dto.setAdmin(post.getAdmin().getLogin());
        dto.setDescricao(post.getDescricao());
        dto.setGithub(post.getGithub());
        dto.setId(post.getId());
        dto.setImg(post.getImg());
        dto.setProfile_ids(convertProfList(post.getProfile()));
        dto.setStatus(post.getStatus());
        dto.setTitulo(post.getTitulo());
        return dto;
    }

    public Usuario getUsuarioByToken(String token){
        String usuarioString = jwt.obterLoginUsuario(token);
        Usuario usuario = usuarioRepository.findByLogin(usuarioString).orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário não encontrado com o token fornecido"));
        return usuario;
    }

    public boolean isValid(String input) {
        String regex = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public void validateAdmin(Post post, String token) {
        Usuario usuario = getUsuarioByToken(token);
        if (!post.getAdmin().equals(usuario)) {
            throw new UnauthorizedUpdateException(HttpStatus.FORBIDDEN ,  "Not authorized to update this post");
        }
    }


    public List<PostDTO> convertPostsToDTOs(List<Post> posts) {
        List<PostDTO> dto = new ArrayList<>();
        for (Post post : posts) {
            dto.add(converPostToDTO(post));
        }
        return dto;
    }
}
