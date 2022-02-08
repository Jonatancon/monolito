package com.pragma.monolito.business.impl;

import com.pragma.monolito.exception.CoreException;
import com.pragma.monolito.model.ImageMongo;
import com.pragma.monolito.repository.ImagesMongoRepository;
import com.pragma.monolito.repository.PersonRepository;
import com.pragma.monolito.util.ValidatorsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ImagesMongoImplTest {

    @InjectMocks
    private ImagesMongoImpl imagesMongoImpl;

    @Mock
    private ImagesMongoRepository imagesMongoRepository;

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
        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(imagesMongoRepository.save(any(ImageMongo.class))).thenReturn(any(ImageMongo.class));
        assertEquals(200, imagesMongoImpl.createImage(fileMock, 1L).getStatus());
    }

    @Test
    void createFail() {
        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(imagesMongoRepository.save(any(ImageMongo.class))).thenThrow(CoreException.class);
        assertEquals(500, imagesMongoImpl.createImage(fileMock, 1L).getStatus());
    }

    @Test
    void updateImage() {
        when(imagesMongoRepository.existsById(anyLong())).thenReturn(true);
        when(imagesMongoRepository.findById(anyLong())).thenReturn(Optional.of(new ImageMongo()));
        when(imagesMongoRepository.save(any(ImageMongo.class))).thenReturn(any(ImageMongo.class));

        assertEquals(202, imagesMongoImpl.updateImage(fileMock, 1L).getStatus());
    }

    @Test
    void updateImageFail() {
        when(imagesMongoRepository.existsById(anyLong())).thenReturn(true);
        when(imagesMongoRepository.findById(anyLong())).thenReturn(Optional.of(new ImageMongo()));
        when(imagesMongoRepository.save(any(ImageMongo.class))).thenThrow(CoreException.class);

        assertEquals(500, imagesMongoImpl.updateImage(fileMock, 1L).getStatus());
    }

    @Test
    void deleteImage() {
        when(imagesMongoRepository.existsById(anyLong())).thenReturn(true);
        assertEquals(202, imagesMongoImpl.deleteImage(1L).getStatus());
    }

    @Test
    void deleteAllImagesOfPerson() {
        assertDoesNotThrow(() ->imagesMongoImpl.deleteAllImagesOfPerson(1L));
    }

    @Test
    void getAllImages() {
        when(imagesMongoRepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, imagesMongoImpl.getAllImages().size());
    }

    @Test
    void getAllImagesFails() {
        when(imagesMongoRepository.findAll()).thenThrow(CoreException.class);
        assertThrowsExactly(CoreException.class, () -> imagesMongoImpl.getAllImages());
    }

    @Test
    void validatorFile() {
        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "".getBytes());
        assertThrowsExactly(CoreException.class, () -> imagesMongoImpl.validatorFile(file));
    }

    @Test
    void existFile() {
        when(imagesMongoRepository.existsById(anyLong())).thenReturn(false);
        assertThrowsExactly(CoreException.class, () -> imagesMongoImpl.existFile(1L));

    }
}