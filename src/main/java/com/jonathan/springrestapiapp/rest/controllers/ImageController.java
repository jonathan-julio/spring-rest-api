package com.jonathan.springrestapiapp.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.springrestapiapp.model.Image;
import com.jonathan.springrestapiapp.service.ImageService;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/image")
public class ImageController {

    private final String uploadDir = "src/main/java/com/jonathan/springrestapiapp/public/";

    @Autowired
    ImageService imageService;

    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(uploadDir).resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Defina o tipo de conteúdo apropriado para sua imagem
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<String> getImage(@PathVariable Integer imageId) {
        // Busca a imagem no banco de dados pelo ID
        Image image = imageService.findById(imageId);

        if (image != null && image.getImage() != null) {
            // Construa o URL para acessar a imagem
            String imageUrl = "/image/view/" + imageId; // Altere conforme necessário para a rota correta do seu aplicativo

            return ResponseEntity.ok().body(imageUrl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view/{imageId}")
    public ResponseEntity<Resource> viewImage(@PathVariable Integer imageId) {
        // Busca a imagem no banco de dados pelo ID
        Image image = imageService.findById(imageId);

        if (image != null && image.getImage() != null) {
            // Converte os bytes da imagem em um recurso ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(image.getImage());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg") // Defina o tipo de conteúdo apropriado para sua imagem
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
