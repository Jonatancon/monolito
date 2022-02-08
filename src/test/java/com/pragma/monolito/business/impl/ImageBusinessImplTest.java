package com.pragma.monolito.business.impl;

import com.pragma.monolito.exception.CoreException;
import com.pragma.monolito.model.Image;
import com.pragma.monolito.model.ImageMongo;
import com.pragma.monolito.model.Person;
import com.pragma.monolito.repository.ImageRepository;
import com.pragma.monolito.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ImageBusinessImplTest {

    @InjectMocks
    private ImageBusinessImpl imagesImpl;

    @Mock
    private ImageRepository imagesRepository;

    @Mock
    private PersonRepository personRepository;

    MockMultipartFile fileMock;

    @BeforeEach
    void setUp() {
        fileMock = new MockMultipartFile("data", "filename.txt", "text/plain", "sdfsdsc".getBytes());
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createImage() {
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(new Person()));
        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(imagesRepository.save(any(Image.class))).thenReturn(any(Image.class));
        assertEquals(200, imagesImpl.createImage(fileMock, 1L).getStatus());
    }

    @Test
    void createFail() {
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(new Person()));
        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(imagesRepository.save(any(Image.class))).thenThrow(CoreException.class);
        assertEquals(500, imagesImpl.createImage(fileMock, 1L).getStatus());
    }

    @Test
    void updateImage() {
        when(imagesRepository.existsById(anyLong())).thenReturn(true);
        when(imagesRepository.findById(anyLong())).thenReturn(Optional.of(new Image()));
        when(imagesRepository.save(any(Image.class))).thenReturn(any(Image.class));

        assertEquals(202, imagesImpl.updateImage(fileMock, 1L).getStatus());
    }

    @Test
    void updateImageFail() {
        when(imagesRepository.existsById(anyLong())).thenReturn(true);
        when(imagesRepository.findById(anyLong())).thenReturn(Optional.of(new Image()));
        when(imagesRepository.save(any(Image.class))).thenThrow(CoreException.class);

        assertEquals(500, imagesImpl.updateImage(fileMock, 1L).getStatus());
    }

    @Test
    void deleteImage() {
        when(imagesRepository.existsById(anyLong())).thenReturn(true);
        assertEquals(202, imagesImpl.deleteImage(1L).getStatus());
    }

    @Test
    void deleteAllImagesOfPerson() {
        assertDoesNotThrow(() -> imagesImpl.deleteAllImagesOfPerson(1L));
    }

    @Test
    void getAllImages() {
        when(imagesRepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, imagesImpl.getAllImages().size());
    }

    @Test
    void getAllImagesFails() {
        when(imagesRepository.findAll()).thenThrow(CoreException.class);
        assertThrowsExactly(CoreException.class, () -> imagesImpl.getAllImages());
    }

    @Test
    void validatorFile() {
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "".getBytes());
        assertThrowsExactly(CoreException.class, () -> imagesImpl.validatorFile(file));
    }

    @Test
    void existFile() {
        when(imagesRepository.existsById(anyLong())).thenReturn(false);
        assertThrowsExactly(CoreException.class, () -> imagesImpl.existFile(1L));

    }
}