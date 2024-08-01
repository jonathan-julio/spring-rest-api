package com.jonathan.springrestapiapp.service;

import com.jonathan.springrestapiapp.model.Person;
import com.jonathan.springrestapiapp.rest.dto.PersonDTO;
import com.jonathan.springrestapiapp.rest.dto.PersonPatchDTO;




public interface PersonService {

    public Person save(Person person );
    public PersonDTO getPersonById(Integer id );
    public PersonDTO getPersonByUserId(Integer id);
    public PersonDTO updatePerson(PersonDTO dto, String token );
    public PersonPatchDTO patchPerson(PersonPatchDTO dto, String token );


}
