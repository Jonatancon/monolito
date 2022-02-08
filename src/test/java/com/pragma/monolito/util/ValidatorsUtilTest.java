package com.pragma.monolito.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorsUtilTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isObjectNull() {
        assertTrue(ValidatorsUtil.isObjectNull(null));
        assertFalse(ValidatorsUtil.isObjectNull("Hola"));
    }

    @Test
    void isNullOrEmpty() {
        assertTrue(ValidatorsUtil.isNullOrEmpty("   "));
        assertFalse(ValidatorsUtil.isObjectNull("Hola"));
    }

    @Test
    void isEmptyFile() {
        assertFalse(ValidatorsUtil.isEmptyFile(new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        }));
    }

    @Test
    void isValidEmail() {
        assertTrue(ValidatorsUtil.isValidEmail("jonatan.restrepo@pragma.com.co"));
        assertFalse(ValidatorsUtil.isValidEmail("jonatan.restrepopragma"));
    }
}