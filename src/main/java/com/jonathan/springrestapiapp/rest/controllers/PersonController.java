package com.jonathan.springrestapiapp.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.rest.dto.PersonDTO;
import com.jonathan.springrestapiapp.service.PersonService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;




@RequestMapping("/api/person")
@SecurityRequirement(name = "Bearer Authentication")
@RestController //anotação especializadas de controller - todos já anotados com response body!
public class PersonController {

    @Autowired
    private PersonService personService;

    

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person save( @RequestBody Person person ){
        return personService.save(person);
    }


    @GetMapping("{id}")
    public PersonDTO getPersonById( @PathVariable @Valid Integer id ){
        return personService.getPersonById(id);
    }

    //UPDATE
    @PutMapping("edit")
    public PersonDTO updatePerson(@RequestBody @Valid PersonDTO dto, HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        return personService.updatePerson(dto,token);
    }

}
