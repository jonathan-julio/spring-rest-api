package com.jonathan.springrestapiapp.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.model.Log;
import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.repository.ProfileRepository;
import com.jonathan.springrestapiapp.rest.dto.ProfileDTO;
import com.jonathan.springrestapiapp.service.LogService;
import com.jonathan.springrestapiapp.service.ProfileService;
import com.jonathan.springrestapiapp.service.Utils;


@Component
public class  ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UsuarioServiceImpl usuarioServiceImpl;

    @Autowired
    Utils utils;

    @Override
    public Profile save(Profile profile ){
        return profileRepository.save(profile);
    }

    @Override
    public Profile getClienteById(Integer id ){
        return profileRepository
                .findById(id)
                .orElseThrow(() -> //se nao achar lança o erro!
                        new AlgoNaoEncontradoException(
                                "Cliente não encontrado"));
    }

    @Override
    public Usuario updatProfile(ProfileDTO profile, String token) {
        String usuarioString =  utils.jwt.obterLoginUsuario(token);
        Optional<Usuario> usuario = usuarioServiceImpl.findByLogin(usuarioString);
       
        Log log = new Log(usuario.get(), "ProfileServiceImpl.updatProfile", "Update profile de: " + usuario.get().getPerson().getProfile().toString() );
        utils.logService.save(log);
       
        usuario.get().getPerson().setProfile(Profile
        .builder()
        .about(profile.getAbout())
        .background(profile.getBackground())
        .color(profile.getColor())
        .post(usuario.get().getPosts())
        .build());
        return usuarioServiceImpl.salvar(usuario.get());

        
    };

    public List<Profile> findAllById (List<Integer> ids){
        return profileRepository.findAllById(ids);
    }

}
