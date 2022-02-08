package com.pragma.monolito.mapper;

import com.pragma.monolito.domain.PersonDto;
import com.pragma.monolito.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonDto personToPersonDto(Person person);

    Person personDtoToPerson(PersonDto personDto);

}
