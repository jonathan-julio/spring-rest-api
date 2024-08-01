package com.jonathan.springrestapiapp.service;


import com.jonathan.springrestapiapp.model.Image;

public interface ImageService {

    public Image findById(Integer id);

    public Image save(Image image);

}
