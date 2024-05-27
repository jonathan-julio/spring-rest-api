package com.jonathan.springrestapiapp.service;

import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.model.Usuario;
import com.jonathan.springrestapiapp.rest.dto.PersonDTO;




public interface PersonService {

    public Person save(Person person );
    public PersonDTO getPersonById(Integer id );
    public Person getPersonByUserId(Integer id);
    public PersonDTO updatePerson(PersonDTO dto, String token );


}
