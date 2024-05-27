package com.jonathan.springrestapiapp.service;

import java.util.List;

import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.ProfileDTO;



public interface ProfileService {

     public Profile save(Profile profile);
     public Profile getClienteById(Integer id);
     public Usuario updatProfile(ProfileDTO profile, String token);
     public List<Profile> findAllById (List<Integer> ids);
     
}
