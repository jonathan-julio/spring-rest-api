package com.jonathan.springrestapiapp.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.jonathan.springrestapiapp.model.Image;



public interface ImageRepository extends JpaRepository<Image,Integer>{


}
