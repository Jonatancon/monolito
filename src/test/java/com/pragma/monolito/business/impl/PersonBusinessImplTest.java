package com.pragma.monolito.business.impl;

import com.pragma.monolito.domain.PersonDto;
import com.pragma.monolito.exception.CoreException;
import com.pragma.monolito.model.Person;
import com.pragma.monolito.repository.PersonRepository;
import com.pragma.monolito.util.ValidatorsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonBusinessImplTest {
    @InjectMocks
    private PersonBusinessImpl personBusinessImpl;
    @Mock
    private PersonRepository personRepository;

    PersonDto personDto = PersonDto.builder().id(1L).location("Medellin").phone("321 640 8728")
            .name("Jonatan Stiven").lastName("Restrepo Lora").email("Jonatan.restrepo@pragma.com.co")
            .address("123 456").build();

    Person person = new Person();

    @BeforeEach
    void setUp() {
        person.setLastName("stark");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPerson() {
        when(personRepository.save(any(Person.class))).thenReturn(new Person());
        assertEquals(201, personBusinessImpl.createPerson(personDto).getStatus());
    }

    @Test
    void createPersonFail() {
        when(personRepository.save(any(Person.class))).thenThrow(CoreException.class);
        assertEquals(500, personBusinessImpl.createPerson(personDto).getStatus());
    }

    @Test
    void updatePerson() {
        when(personRepository.existsById(anyLong())).thenReturn(true);
        assertEquals(202, personBusinessImpl.updatePerson(personDto).getStatus());
    }

    @Test
    void updatePersonFail() {
        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(personRepository.save(any(Person.class))).thenThrow(CoreException.class);
        assertEquals(500, personBusinessImpl.updatePerson(personDto).getStatus());
    }

    @Test
    void deletePerson() {
        when(personRepository.existsById(anyLong())).thenReturn(true);
        assertEquals(202, personBusinessImpl.deletePerson(1L).getStatus());
    }

    @Test
    void getAllPerson() {
        when(personRepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, personBusinessImpl.getAllPerson().size());
    }

    @Test
    void getAllPersonFail() {
        when(personRepository.findAll()).thenThrow(CoreException.class);
        assertThrowsExactly(CoreException.class, () -> personBusinessImpl.getAllPerson());
    }

    @Test
    void findPerson() {
        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        assertEquals("stark", personBusinessImpl.findPerson(1L).getLastName());
    }

    @Test
    void findPersonFail() {
        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(personRepository.findById(anyLong())).thenThrow(CoreException.class);
        assertThrowsExactly(CoreException.class, () -> personBusinessImpl.findPerson(1L));
    }

    @Test
    void verifyPersonInfo() {
        personDto.setEmail("  ");
        assertThrowsExactly(CoreException.class, () -> personBusinessImpl.verifyPersonInfo(personDto));
    }

    @Test
    void verifyNotNull() {
        assertThrowsExactly(CoreException.class, () -> personBusinessImpl.verifyNotNull(null));
    }

    @Test
    void verifyEmailErrorEmail() {
        assertThrowsExactly(CoreException.class, () -> personBusinessImpl.verifyEmail("jonatan lora"));
    }

    @Test
    void verifyEmailDuplicateEmail() {
        when(personRepository.existsPersonByEmail(anyString())).thenReturn(true);
        assertThrowsExactly(CoreException.class, () -> personBusinessImpl.verifyEmail("jonatan@pragma.com"));
    }

    @Test
    void existPerson() {
        when(personRepository.existsById(anyLong())).thenReturn(false);
        assertThrowsExactly(CoreException.class, () -> personBusinessImpl.existPerson(1L));
    }

    @Test
    void getPerson() {
        when(personRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        assertThrowsExactly(NullPointerException.class, () -> personBusinessImpl.getPerson(1L));
    }
}