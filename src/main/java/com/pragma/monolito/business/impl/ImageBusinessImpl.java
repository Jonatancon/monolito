package com.pragma.monolito.business.impl;

import com.pragma.monolito.business.ImageBusiness;
import com.pragma.monolito.domain.ImageDto;
import com.pragma.monolito.domain.Response;
import com.pragma.monolito.domain.enums.CodeErrors;
import com.pragma.monolito.exception.CoreException;
import com.pragma.monolito.model.Image;
import com.pragma.monolito.repository.ImageRepository;
import com.pragma.monolito.repository.PersonRepository;
import com.pragma.monolito.util.ValidatorsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ImageBusinessImpl extends PersonBusinessImpl implements ImageBusiness {
    private final ImageRepository imageRepository;

    public ImageBusinessImpl(ImageRepository imageRepository, PersonRepository personRepository) {
        super(personRepository);
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public Response createImage(MultipartFile file, Long personId) {
        verifyNotNull(personId);
        validatorFile(file);
        existPerson(personId);
        verifyNotNull(file);

        Image image = new Image();

        try{
            image.setPerson(getPerson(personId));
            image.setImageData(file.getBytes());

            imageRepository.save(image);
            return Response.builder().developerMessage("Image save").userMessage("Imagen guardada al usuario")
                    .status(200).moreInfo("API").build();
        }catch (CoreException e) {
            log.error("Error en la creacion de la foto", e);
        } catch (IOException e) {
            log.error("Error en convertir en bytes", e);
        }
        return Response.builder().userMessage("No se pudo guardar la imagen").developerMessage("No save image")
                .status(500).errorCode(CodeErrors.I_001.name()).moreInfo("Error interno del servidor").build();
    }

    @Override
    @Transactional
    public Response updateImage(MultipartFile file, Long fileId) {
        verifyNotNull(fileId);
        existFile(fileId);
        verifyNotNull(file);
        try {
            Image image = imageRepository.findById(fileId).orElseThrow();
            image.setImageData(file.getBytes());

            imageRepository.save(image);

            return Response.builder().status(202).developerMessage("Image Update")
                    .userMessage("La imagen fue cambiada con exito").build();
        }catch (CoreException e) {
            log.error("No se pudo actualizar la información", e);
        } catch (IOException e) {
            log.error("Error a la hora de convertir el file", e);
        }

        return Response.builder().userMessage("No se pudo actualizar la imagen")
                .developerMessage("Error interno del server").errorCode(CodeErrors.I_001.name()).status(500).build();
    }

    @Override
    @Transactional
    public Response deleteImage(Long id) {
        existFile(id);
        try {
            imageRepository.deleteById(id);

            return Response.builder().userMessage("Se elimino la imagen").developerMessage("Delete Imagen Success")
                    .status(202).build();
        }catch (CoreException e) {
            log.error("Error al eliminar", e);
        }
        return Response.builder().developerMessage("Error en el servidor").userMessage("No se pudo eliminar la imagen")
                .status(500).errorCode(CodeErrors.I_001.name()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageDto> getAllImages() {
        try {
            return imageRepository.findAll().stream().map(img -> ImageDto.builder().imageData(img.getImageData())
                    .id(img.getId()).personId(img.getPerson().getId()).hasCode(img.getImageHashCode().longValue())
                    .build()).toList();
        }catch (CoreException e) {
            log.error("No se pudo consultar en la base de datos", e);
        }

        throw new CoreException("No se pudo consultar en la base de datos", "Error al conectar en la base de datos",
                500, CodeErrors.I_001.name());
    }

    @Override
    @Transactional
    public void deleteAllImagesOfPerson(Long personId) {
        verifyNotNull(personId);
        imageRepository.deleteAllByPerson_Id(personId);
    }

    public void existFile(Long id) throws CoreException{
        if (!imageRepository.existsById(id)) {
            throw new CoreException("no hay registro", "No hay ningúna imagen con ese id", 404, CodeErrors.D_001.name());
        }
    }

    public void validatorFile(MultipartFile file) throws CoreException{
        if (ValidatorsUtil.isEmptyFile(file)) {
            throw new CoreException("Empty file", "No hay ningúna imagen por favor suba una",
                    400, CodeErrors.D_001.name());
        }
    }


}
