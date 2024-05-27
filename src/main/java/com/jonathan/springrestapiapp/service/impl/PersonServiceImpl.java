package com.jonathan.springrestapiapp.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonathan.springrestapiapp.exception.AlgoNaoEncontradoException;
import com.jonathan.springrestapiapp.exception.RegraNegocioException;
import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.repository.PersonRepository;
import com.jonathan.springrestapiapp.rest.dto.PersonDTO;
import com.jonathan.springrestapiapp.security.JwtService;
import com.jonathan.springrestapiapp.service.PersonService;
import com.jonathan.springrestapiapp.service.Utils;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private  PersonRepository personRepository;
    @Autowired
    private Utils utils;
    
    @Override
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Override
    public PersonDTO getPersonById(Integer id) {
        Person person = personRepository.findById(id)
        .orElseThrow(() -> new AlgoNaoEncontradoException("Pessoa não encontrada"));
        return utils.convertePersonToDTO(person);
      
        
    }

    @Override
    public Person getPersonByUserId(Integer id) {
        return personRepository.findByUserId(id)
                .orElseThrow(() -> new AlgoNaoEncontradoException("Pessoa não encontrada"));
    }

    @Override
    public PersonDTO updatePerson(PersonDTO dto, String token) {
        Usuario usuario = utils.getUsuarioByToken(token);
        Person person = usuario.getPerson();
        person.setData(dto.getData());
        person.setNome(dto.getNome());
        person.setSexo(dto.getSexo());

        return utils.convertePersonToDTO(save(person));
    }
}
