package com.jonathan.springrestapiapp.service;

import com.jonathan.springrestapiapp.model.Profile;
import com.jonathan.springrestapiapp.rest.dto.ProfileDTO;



public interface ProfileService {

     public Profile save(Profile profile);
     public Profile getClienteById(Integer id);
     public Profile getClienteByIdUser(Integer id);
     public ProfileDTO updatProfile(ProfileDTO profile, String token);
     public ProfileDTO patchHtml(Boolean isHtml, String token);
     
}
