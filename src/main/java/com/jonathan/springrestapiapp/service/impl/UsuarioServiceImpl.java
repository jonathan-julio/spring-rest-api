package com.jonathan.springrestapiapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jonathan.springrestapiapp.enums.AcessoUsuario;
import com.jonathan.springrestapiapp.enums.StatusPost;
import com.jonathan.springrestapiapp.enums.UserRole;
import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.MyException;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;
import com.jonathan.springrestapiapp.exception.SenhaInvalidaException;
import com.jonathan.springrestapiapp.model.Log;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.AcessoDTO;
import com.jonathan.springrestapiapp.rest.dto.CredenciaisDTO;
import com.jonathan.springrestapiapp.rest.dto.LogDTO;
import com.jonathan.springrestapiapp.rest.dto.PapelDTO;
import com.jonathan.springrestapiapp.rest.dto.PostDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchAdmDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPostDTO;
import com.jonathan.springrestapiapp.rest.dto.UserCreaterDTO;
import com.jonathan.springrestapiapp.rest.dto.UserDTO;
import com.jonathan.springrestapiapp.rest.dto.UsuarioDTO;
import com.jonathan.springrestapiapp.service.Utils;

import jakarta.transaction.Transactional;

@Component
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private Utils utils;

    public Usuario created(UserCreaterDTO userDto) {
        Usuario usuario = utils.converteUsuarioDTO(userDto);
        checkPassword(userDto.getSenha());
        checkUsuario(usuario);
        Usuario user = salvar(criptografar(usuario));
        Log log = new Log(user, "UsuarioServiceImpl.salvarUsuario", "Usuario salvo no BD");
        utils.logService.save(log);
        return user;

    }

    @Transactional
    public void checkPassword(String password) {
        if (!utils.isValid(password)) {
            throw new SenhaInvalidaException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "A senha deve ter no mínimo 8 caracteres e incluir 2 números.");
        }
    }

    public void checkUsuario(Usuario usuario) {
        if (utils.usuarioRepository.existsByLogin(usuario.getLogin())) {
            throw new RegraNegocioException(HttpStatus.UNAUTHORIZED, "Login já existe.");
        }
    }

    public void checkLastPassword(Usuario usuario, SenhaPatchDTO dto) {
        if (!passwordEncoder.matches(dto.getLastPassword(), usuario.getSenha())) {
            throw new RegraNegocioException(HttpStatus.BAD_GATEWAY, "Senha anterior incorreta.");
        }
    }

    @Transactional
    public Usuario setTipUsuario(Usuario usuario, UserRole tipo) {
        if (tipo.equals(UserRole.ADMIN) || usuario.getRole().equals(UserRole.ADMIN)) {
            Log log = new Log(usuario, "UsuarioServiceImpl.setTipUsuario",
                    "Tentativa de alterar usuario para: " + tipo.name());
            utils.logService.save(log);
            throw new RegraNegocioException(HttpStatus.FORBIDDEN,
                    "Isso é uma ditadura e " + usuario.getLogin() + " será o único admin.");
        } else {
            usuario.setRole(tipo);
            Log log = new Log(usuario, "UsuarioServiceImpl.setTipUsuario",
                    "Usuario teve o tipo mudado para: " + tipo.name());
            utils.logService.save(log);
            return salvar(usuario);
        }
    }

    @Transactional
    public Usuario setAcesso(AcessoDTO acessoUsuario) {
        Usuario user = utils.usuarioRepository.findById(acessoUsuario.getUsuarioID())
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));

        if (acessoUsuario.getAcesso().equals(AcessoUsuario.BLOQUEADO) && user.getRole().equals(UserRole.ADMIN)) {
            throw new RegraNegocioException(HttpStatus.UNPROCESSABLE_ENTITY, "Não pode bloquear acesso do admin.");
        }
        if (!acessoUsuario.getAcesso().equals(AcessoUsuario.LIBERADO)
                && !acessoUsuario.getAcesso().equals(AcessoUsuario.BLOQUEADO)) {
            throw new RegraNegocioException(HttpStatus.UNPROCESSABLE_ENTITY, "Tipo de acesso não permitido.");
        } else {
            user.setAcesso(acessoUsuario.getAcesso());
            Log log = new Log(user, "UsuarioServiceImpl.setAcesso",
                    "Alterar acesso do usuario para " + acessoUsuario);
            utils.logService.save(log);
            return salvar(criptografar(user));
        }
    }

    public Usuario salvar(Usuario usuario) {
        return utils.usuarioRepository.save(usuario);
    }

    private Usuario criptografar(Usuario usuario) {
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuario;
    }

    public UserDetails autenticar(CredenciaisDTO credenciais) {
        UserDetails userDetails = loadUserByUsername(credenciais.getLogin());
        Usuario usuario = utils.usuarioRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário ou senha inválidos"));
        boolean senhasBatem = passwordEncoder.matches(credenciais.getSenha(), userDetails.getPassword());
        if (senhasBatem) {
            Log log = new Log(usuario, "UsuarioServiceImpl.autenticar", "Usuario autenticado");
            utils.logService.save(log);
            return userDetails;
        } else {
            Log log = new Log(usuario, "UsuarioServiceImpl.autenticar", "Usuario não autenticado");
            utils.logService.save(log);
            throw new SenhaInvalidaException(HttpStatus.BAD_REQUEST, "Usuário ou senha inválidos");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = utils.usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário ou senha inválidos"));

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .build();
    }

    public UserDTO findById(Integer id) {
        Usuario usuario = utils.usuarioRepository.findById(id)
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND,
                        "Usuário não encontrado na base de dados."));
        return UserDTO.builder()
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .role(usuario.getRole())
                .person(utils.convertePersonToDTO(usuario.getPerson()))
                .posts(utils.convertPostsToDTOs(usuario.getPosts()))
                .build();
    }

    public Optional<Usuario> findByLogin(String id) {
        return utils.usuarioRepository.findByLogin(id);
    }

    public UserDTO findDtoByLogin(String id) {
        Usuario user = utils.usuarioRepository.findByLogin(id)
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        UserDTO retorno = utils.converteUsuarioToDTO(user);
        List<PostDTO> postPublicado = retorno.getPosts().stream()
                .filter(post -> post.getStatus().equals(StatusPost.PUBLICADO))
                .collect(Collectors.toList());

        retorno.setPosts(postPublicado);
        return retorno;
    }

    public UserDTO patchPassword(SenhaPatchDTO dto, String token) {
        Usuario usuario = utils.getUsuarioByToken(token);
        checkLastPassword(usuario, dto);
        changePassword(dto.getPassword(), usuario);
        return utils.converteUsuarioToDTO(usuario);
    }

    public UserDTO recoverPassword(SenhaPostDTO dto, String token) {
        if (!utils.jwt.tokenValido(token)) {
            throw new RegraNegocioException(HttpStatus.UNAUTHORIZED, "Token expirado.");
        }

        Usuario usuario = utils.getUsuarioByToken(token);
        changePassword(dto.getPassword(), usuario);
        utils.jwt.invalidatedTokens(token);
        return utils.converteUsuarioToDTO(usuario);
    }

    public UserDTO patchPasswordAdmin(SenhaPatchAdmDTO dto) {
        Usuario usuario = utils.usuarioRepository.findById(dto.getUsuarioID())
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        changePassword(dto.getPassword(), usuario);
        return utils.converteUsuarioToDTO(usuario);
    }

    private UserDTO changePassword(String password, Usuario usuario) {
        checkPassword(password);
        usuario.setSenha(password);
        salvar(criptografar(usuario));
        return utils.converteUsuarioToDTO(usuario);
    }

    public UserDTO patchPassword(SenhaPatchDTO dto, Integer id) {
        Usuario usuario = utils.usuarioRepository.findById(id)
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        checkPassword(dto.getPassword());
        usuario.setSenha(dto.getPassword());
        salvar(criptografar(usuario));
        return utils.converteUsuarioToDTO(usuario);
    }

    public UserDTO alterarPapel(PapelDTO dto, String token) {
        Usuario usuario = utils.usuarioRepository.findById(dto.getUsuarioID())
                .orElseThrow(() -> new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        setTipUsuario(usuario, dto.getTipo());
        return utils.converteUsuarioToDTO(usuario);
    }

    private Usuario converteUsuario(Usuario usuario) {
        return Usuario.builder()
                .login(usuario.getLogin())
                .role(usuario.getRole())
                .person(usuario.getPerson())
                .build();
    }

    public List<UserDTO> getAllBloqueados() {
        List<UserDTO> bloqueados = new ArrayList<>();
        List<Usuario> usuarios = utils.usuarioRepository.findAllByAcesso(1);
        for (Usuario usuario : usuarios) {
            bloqueados.add(utils.converteUsuarioToDTO(usuario));
        }
        return bloqueados;
    }

    public List<UsuarioDTO> getAll() {
        List<UsuarioDTO> usuarioDTOs = new ArrayList<>();
        List<Usuario> usuarios = utils.usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            UsuarioDTO user = UsuarioDTO.builder()
                    .id(usuario.getPerson().getProfile().getId())
                    .login(usuario.getLogin())
                    .build();
            usuarioDTOs.add(user);
        }
        return usuarioDTOs;
    }

    public List<LogDTO> getLogsById(Integer id) {
        List<LogDTO> logsDTO = new ArrayList<>();
        List<Log> logs = utils.logService.getAllByUsuario(id);

        for (Log log : logs) {
            LogDTO logDTO = new LogDTO(log.getId(), utils.converteUsuarioToDTO(log.getUsuario()), log.getFunction(),
                    log.getChanger(), log.getData());
            logsDTO.add(logDTO);
        }
        return logsDTO;
    }

    public Usuario alterarAcesso(AcessoDTO acesso) {
        Usuario usuario = setAcesso(acesso);
        return converteUsuario(usuario);
    }
}
