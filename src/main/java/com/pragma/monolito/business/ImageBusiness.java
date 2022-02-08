package com.pragma.monolito.business;

import com.pragma.monolito.domain.ImageDto;
import com.pragma.monolito.domain.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageBusiness {

    Response createImage(MultipartFile file, Long personId);
    Response updateImage(MultipartFile file, Long fileId);
    Response deleteImage(Long id);
    void deleteAllImagesOfPerson(Long personId);
    List<ImageDto> getAllImages();
}
