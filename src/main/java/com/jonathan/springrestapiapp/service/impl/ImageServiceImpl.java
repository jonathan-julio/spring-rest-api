package com.jonathan.springrestapiapp.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.model.Image;
import com.jonathan.springrestapiapp.repository.ImageRepository;
import com.jonathan.springrestapiapp.service.ImageService;

@Component
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;


    @Override
    public Image findById(Integer id) {
        return imageRepository.findById(id).orElseThrow(() -> 
                        new AlgoNaoEncontradoException(HttpStatus.NOT_FOUND,
                                "Imagem não encontrada"));
    }


    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    

    

    
    
}