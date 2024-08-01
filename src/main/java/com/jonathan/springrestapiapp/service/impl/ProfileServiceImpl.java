package com.jonathan.springrestapiapp.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
                        new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND,
                                "Cliente não encontrado"));
    }

    @Override
    public Profile getClienteByIdUser(Integer id ){
        return profileRepository
                .findByUserId(id)
                .orElseThrow(() -> //se nao achar lança o erro!
                        new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND,
                                "Cliente não encontrado"));
    }

    @Override
    public ProfileDTO updatProfile(ProfileDTO dto, String token) {
        String usuarioString =  utils.jwt.obterLoginUsuario(token);
        Usuario usuario = usuarioServiceImpl.findByLogin(usuarioString).orElseThrow(() -> //se nao achar lança o erro!
        new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND,
                "Cliente não encontrado"));
        Profile profile = profileRepository.findById(usuario.getPerson().getProfile().getId()).orElseThrow(() -> //se nao achar lança o erro!
        new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND,
                "Perfil não encontrado"));
       
        /* Log log = new Log(usuario.get(), "ProfileServiceImpl.updatProfile", "Update profile de: " + usuario.get().getPerson().getProfile().toString() );
        utils.logService.save(log); */
       
        profile.setAbout(dto.getAbout());
        profile.setBackground(dto.getBackground());
        profile.setColor(dto.getColor());
        profile.setTexto(dto.getTexto());
        profile.setTextoSecundario(dto.getTextoSecundario());
        return utils.converteProfileToDTO(profileRepository.save(profile)) ;

        
    };
}
