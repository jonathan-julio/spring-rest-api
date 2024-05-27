package com.jonathan.springrestapiapp.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jonathan.springrestapiapp.enums.AcessoUsuario;
import com.jonathan.springrestapiapp.enums.UserRole;
import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;
import com.jonathan.springrestapiapp.exception.SenhaInvalidaException;
import com.jonathan.springrestapiapp.model.*;
import com.jonathan.springrestapiapp.rest.dto.CredenciaisDTO;
import com.jonathan.springrestapiapp.rest.dto.LogDTO;
import com.jonathan.springrestapiapp.rest.dto.PapelDTO;
import com.jonathan.springrestapiapp.rest.dto.SenhaPatchDTO;
import com.jonathan.springrestapiapp.rest.dto.UserCreaterDTO;
import com.jonathan.springrestapiapp.rest.dto.UserDTO;
import com.jonathan.springrestapiapp.service.Utils;

import jakarta.transaction.Transactional;



/*
 * interface do spring security serve para definir o carregam, UserDetailsento de usuários através de uma base de dados
 */
@Component
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private Utils utils;


    public Usuario created(UserCreaterDTO userDto) {
        Usuario usuario = utils.converteUsuarioDTO(userDto);
        checkPassword(usuario);
        checkUsuario(usuario);
        Usuario user = salvar(criptografar(usuario));
        Log log = new Log(user, "UsuarioServiceImpl.salvarUsuario", "Usuario salvo no BD");
        utils.logService.save(log);
        return user;
    }

    @Transactional
    public void checkPassword(Usuario usuario) {
        if (!utils.isValid(usuario.getSenha())) {
            throw new RegraNegocioException("Senha de ter no minimo 8 caracteres e incluir 2 numeros");
        }
    }

    @Transactional
    public void checkUsuario(Usuario usuario) {
        if (utils.usuarioRepository.existsByLogin(usuario.getLogin())) {
            throw new RegraNegocioException("Login já existe.");
        }
    }

    @Transactional
    public Usuario setTipUsuario(Usuario usuario, UserRole tipo) {
        if (tipo.equals(UserRole.ADMIN)) {
            Log log = new Log(usuario, "UsuarioServiceImpl.setTipUsuario", "Tentativa de alterar usuario para: " + tipo.name());
            utils.logService.save(log);
            throw new RegraNegocioException("Isso é uma ditadura so podemos ter um adm");
            
        }else{
            usuario.setRole(tipo);
            Log log = new Log(usuario, "UsuarioServiceImpl.setTipUsuario", "Usuario teve o tipo mudado para: " + tipo.name());
            utils.logService.save(log);
            return salvar(usuario);
        }
        
    }

    @Transactional
    public Usuario setAcesso(Usuario usuario, AcessoUsuario acessoUsuario) {
        if (acessoUsuario.equals(AcessoUsuario.BLOQUEADO) && usuario.getRole().equals(UserRole.ADMIN)) {
            throw new RegraNegocioException("Não pode bloquear acesso do admin.");
        }
        if (!acessoUsuario.equals(AcessoUsuario.LIBERADO) && !acessoUsuario.equals(AcessoUsuario.BLOQUEADO) ) {
            throw new RegraNegocioException("Tipo de acesso não permitido.");
        }else{
            usuario.setAcesso(acessoUsuario);
            Log log = new Log(usuario, "UsuarioServiceImpl.setAcesso", "Alterar acesso do usuario para " + acessoUsuario);
            utils.logService.save(log);
            return salvar(criptografar(usuario));
        }
        
    }
    
    

    public Usuario salvar(Usuario usuario){
        return utils.usuarioRepository.save(usuario);
    }

    private Usuario criptografar(Usuario usuario){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuario;
    }

    public UserDetails autenticar( CredenciaisDTO credenciais ){
        UserDetails userDetails = loadUserByUsername(credenciais.getLogin());
        Usuario usuario = utils.usuarioRepository.findByLogin(userDetails.getUsername()).get();
        boolean senhasBatem = passwordEncoder.matches(credenciais.getSenha(), userDetails.getPassword() );
        if(senhasBatem){
            Log log = new Log(usuario, "UsuarioServiceImpl.autenticar", "Usuario autenticado");
            utils.logService.save(log);
            return userDetails;
        }
        Log log = new Log(usuario, "UsuarioServiceImpl.autenticar", "Usuario não autenticado");
        utils.logService.save(log);
        throw new SenhaInvalidaException();
    }

    /*
     * Ele é responsável por carregar os detalhes do usuário com base no nome de usuário fornecido.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

         // Consulta ao repositório para obter o usuário com base no nome de usuário fornecido
        Usuario usuario = utils.usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));

        // Cria e retorna o objeto UserDetails com os detalhes do usuário
        return User
                .builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .build();

    }

    public UserDTO findById(Integer id) {
        Usuario usuario = utils.usuarioRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado na base de dados."));

            return UserDTO
                .builder()
                .login(usuario.getLogin())
                .admin(usuario.getRole())
                .person(utils.convertePersonToDTO(usuario.getPerson()))
                .posts(usuario.getPosts())
                .build();
    }

    public Optional<Usuario> findByLogin(String id) {
        return utils.usuarioRepository.findByLogin(id);
    }

    public UserDTO  patchPassaword(SenhaPatchDTO dto, String token)  {
        Usuario usuario = utils.getUsuarioByToken(token);
        usuario.setSenha(dto.getPassword());
        checkPassword(usuario);
        salvar(criptografar(usuario));
        return utils.converteUsuarioToDTO(usuario);
    }

    public UserDTO  patchPassaword(SenhaPatchDTO dto, Integer id)  {
        Usuario usuario = utils.usuarioRepository.findById(id).orElseThrow(() -> new AlgoNaoEncontradoException("Usuario nao encontrado."));
        usuario.setSenha(dto.getPassword());
        checkPassword(usuario);
        salvar(criptografar(usuario));
        return utils.converteUsuarioToDTO(usuario);
    }

    public UserDTO alterarPapel(PapelDTO dto, String token)  {
        Usuario _usuario = utils.usuarioRepository.findById(dto.getUsuarioID())
        .orElseThrow(() -> new AlgoNaoEncontradoException("Usuario nao encontrado."));
        setTipUsuario(_usuario, dto.getTipo());
        return  utils.converteUsuarioToDTO(_usuario);
    }

    private Usuario converteUsuario(Usuario usuario){
        return Usuario
        .builder()
        .login(usuario.getLogin())
        .role(usuario.getRole())
        .person(usuario.getPerson())
        .build();
    }

    public List<UserDTO> getAllBloqueados()  {
        List<UserDTO> bloqueados = new ArrayList();
        List<Usuario> usuarios = utils.usuarioRepository.findAllByAcesso(1);
        for (Usuario usuario : usuarios) {
            bloqueados.add(utils.converteUsuarioToDTO(usuario));
        }
        return bloqueados;
    }

    public List<LogDTO> getLogsById(Integer id)  {
        List<LogDTO> logsDTO = new ArrayList<>();
        List<Log> logs = utils.logService.getAllByUsuario(id) ;

        for (Log log : logs) {
            LogDTO logDTO = new LogDTO(log.getId(), utils.converteUsuarioToDTO(log.getUsuario()), log.getFunction(), log.getChanger(), log.getData());
            logsDTO.add(logDTO);
            
        }
        return logsDTO;
    }

    public Usuario alterarAcesso(AcessoUsuario acesso, String token)  {
        Usuario usuario = utils.getUsuarioByToken(token);
        Usuario _usuario =  setAcesso(usuario, acesso);
        return converteUsuario(_usuario);
    }

}
