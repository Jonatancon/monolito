package com.pragma.monolito.business.impl;

import com.pragma.monolito.business.PersonBusiness;
import com.pragma.monolito.domain.PersonDto;
import com.pragma.monolito.domain.Response;
import com.pragma.monolito.domain.enums.CodeErrors;
import com.pragma.monolito.exception.CoreException;
import com.pragma.monolito.mapper.PersonMapper;
import com.pragma.monolito.model.Person;
import com.pragma.monolito.repository.PersonRepository;
import com.pragma.monolito.util.ValidatorsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
public class PersonBusinessImpl implements PersonBusiness {

    private final PersonRepository personRepository;

    public PersonBusinessImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public Response createPerson(PersonDto personDto) {

        verifyNotNull(personDto);
        verifyPersonInfo(personDto);
        verifyEmail(personDto.getEmail());

        try {
            Person person = PersonMapper.INSTANCE.personDtoToPerson(personDto);

            personRepository.save(person);

            return Response.builder().userMessage("Persona creada en la base de datos").status(201)
                    .errorCode("").developerMessage("Se guardo la persona en la base de datos correctamente")
                    .moreInfo("API").build();

        }catch (CoreException e) {
            log.error("Error al crear la persona", e);
        }
        return Response.builder().errorCode(CodeErrors.I_001.name())
                .developerMessage("Error al crear la persona en la base de datos")
                .userMessage(CodeErrors.NO_SE_COMPLETO_EL_PROCEDIMIENTO_ERROR_INTERNO.name()).status(500).build();
    }

    @Override
    @Transactional
    public Response updatePerson(PersonDto personDto) {

        verifyNotNull(personDto);
        existPerson(personDto.getId());

        try {
            Person person = PersonMapper.INSTANCE.personDtoToPerson(personDto);

            personRepository.save(person);

           return Response.builder().userMessage("Persona actualizada en la base de datos").status(202)
                   .developerMessage("Se actualizo la información de la persona").build();

        }catch (CoreException e) {
            log.error("Error no se pudo actualizar la persona", e);
        }

        return Response.builder().errorCode(CodeErrors.I_001.name())
                .developerMessage("Error al actualizar los datos de la persona")
                .userMessage(CodeErrors.NO_SE_COMPLETO_EL_PROCEDIMIENTO_ERROR_INTERNO.name()).status(500).build();
    }

    @Override
    @Transactional
    public Response deletePerson(Long id) {
        existPerson(id);

        try {
            personRepository.deleteById(id);

            return Response.builder().userMessage("La petición para eliminar a la persona se completo satisfactoriamente " +
                            "junto con todas las imagenes de la persona")
                    .status(202).developerMessage("Se elimino el registro de la base de datos").build();

        }catch (CoreException e) {
            log.error("Error al tratar de eliminar a la persona", e);
        }

        return Response.builder().errorCode(CodeErrors.I_001.name()).developerMessage("Error eliminar el registro")
                .userMessage(CodeErrors.NO_SE_COMPLETO_EL_PROCEDIMIENTO_ERROR_INTERNO.name()).status(500).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDto> getAllPerson() {
        try {
            return personRepository.findAll().stream().map(person -> PersonDto.builder().id(person.getId())
                    .address(person.getAddress()).name(person.getName()).lastName(person.getLastName())
                    .email(person.getEmail()).phone(person.getPhone()).location(person.getLocation()).build()).toList();
        }catch (CoreException e){
            log.error("Error al traer la informacion", e);
        }
        throw new CoreException("Error Data", "Algo anda mal con obtener la información, intenta mas tarde", 500,
                CodeErrors.I_001.name());
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto findPerson(Long id) {
        existPerson(id);

        try {
            Person person = personRepository.findById(id).orElseThrow();

            return PersonMapper.INSTANCE.personToPersonDto(person);
        }catch (CoreException e) {
            log.error("No se pudo consultar a la base de datos", e);
        }

        throw new CoreException("Missing data", "Algo anda mal con obtener la información, intenta mas tarde", 500,
                CodeErrors.I_001.name());
    }

    public void verifyPersonInfo(PersonDto personDto) throws CoreException {

        if (ValidatorsUtil.isNullOrEmpty(personDto.getName()) || ValidatorsUtil.isNullOrEmpty(personDto.getLastName()) ||
                ValidatorsUtil.isNullOrEmpty(personDto.getEmail()) || ValidatorsUtil.isNullOrEmpty(personDto.getPhone()) ||
                ValidatorsUtil.isNullOrEmpty(personDto.getAddress()) ||
                ValidatorsUtil.isNullOrEmpty(personDto.getLocation())) {

            throw new CoreException("Data incorrect", "Algo anda mal con la información por favor revizala", 400,
                    CodeErrors.D_001.name());
        }
    }

    public void verifyNotNull(Object object) throws CoreException{
        if (ValidatorsUtil.isObjectNull(object)) {
            throw new CoreException("Not found Data", "No hay ningúna información, porfavor verifique", 400,
                    CodeErrors.D_001.name());
        }
    }

    public void verifyEmail(String email) throws CoreException{
        if (!ValidatorsUtil.isValidEmail(email)) {
            throw new CoreException("Invalid email", "El correo electronico esta incorrecto", 400,
                    CodeErrors.E_002.name());
        }
        if (personRepository.existsPersonByEmail(email)) {
            throw new CoreException("Some person is using email", "Opps este correo electronico esta en uso", 400,
                    CodeErrors.E_001.name());
        }
    }

    public void existPerson(Long id) throws CoreException{
        if (!personRepository.existsById(id)) {
            throw new CoreException("Not found data", "No hay ningúna persona registrada con ese id", 404,
                    CodeErrors.D_002.name());
        }
    }

    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow();
    }
}
