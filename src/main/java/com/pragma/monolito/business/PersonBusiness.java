package com.pragma.monolito.business;

import com.pragma.monolito.domain.PersonDto;
import com.pragma.monolito.domain.Response;

import java.util.List;

public interface PersonBusiness {

    Response createPerson(PersonDto person);
    Response updatePerson(PersonDto person);
    Response deletePerson(Long id);
    List<PersonDto> getAllPerson();
    PersonDto findPerson(Long id);
}
