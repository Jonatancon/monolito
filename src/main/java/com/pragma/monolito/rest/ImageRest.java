package com.pragma.monolito.rest;

import com.pragma.monolito.business.ImageBusiness;
import com.pragma.monolito.domain.ImageDto;
import com.pragma.monolito.domain.Response;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(ImageRest.IMAGE)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImageRest {

    public static final String IMAGE = "/api/v1/image";
    public static final String SAVE = "/save/{personId}";
    public static final String UPDATE = "/update/{fileId}";
    public static final String DELETE = "/delete/{fileId}";
    public static final String GET_ALL = "/get-all";

    private final ImageBusiness imageBusiness;

    @Autowired
    public ImageRest(@Qualifier("imagesMongoImpl") ImageBusiness imageBusiness) {
        this.imageBusiness = imageBusiness;
    }

    @ApiOperation(value = "Permite Guardar una imagen en el sistema", notes = "Guarda una imagen en la base de datos" +
            " sobre la tabla IMAGE", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiParam(name = "file", value = "Imagen a guardar en la base de datos", required = true, type = "MultipartFile")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "La imagen fue guardada exitosamente",
            response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al guardar la imagen, verifique info",
                    response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al guardar la imagen, no hay ningúna persona con ese id",
                    response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Error al guardar la imagen, error interno del server",
                    response = ResponseEntity.class)})
    @PostMapping(SAVE)
    public ResponseEntity<Response> saveImage(@RequestParam("file") MultipartFile file, @PathVariable Long personId) {
        Response res = this.imageBusiness.createImage(file, personId);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @ApiOperation(value = "Permite Actualizar una imagen en el sistema", notes = "Actualiza la imagen en la base de datos" +
            " sobre la tabla IMAGE", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiParam(name = "file", value = "Imagen a guardar en la base de datos", required = true, type = "MultipartFile")
    @ApiResponses(value = { @ApiResponse(code = 202, message = "La imagen actualizada",
            response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al actualizar la imagen, verifique info",
                    response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al actualizar la imagen, no hay ningúna persona con ese id",
                    response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Error al actualizar la imagen, error interno del server",
                    response = ResponseEntity.class)})
    @PostMapping(UPDATE)
    public ResponseEntity<Response> updateImage(@RequestParam("file") MultipartFile file, @PathVariable Long fileId) {
        Response res = this.imageBusiness.updateImage(file, fileId);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @ApiOperation(value = "Permite eliminar la foto del sistema", notes = "Elimina la foto de la base de datos" +
            " pero solo la del id seleccionado", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiParam(name = "fileId", value = "id de la imagen a eliminar", required = true, type = "Long")
    @ApiResponses(value = { @ApiResponse(code = 202, message = "La imagen imagen fue eliminada",
            response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al eliminar la imagen, verifique info",
                    response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al eliminar la imagen, no hay ningúna imagen con ese id",
                    response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Error al eliminar la imagen, error interno del server",
                    response = ResponseEntity.class)})
    @DeleteMapping(DELETE)
    public ResponseEntity<Response> deleteImage(@PathVariable Long fileId) {
        Response res = this.imageBusiness.deleteImage(fileId);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @ApiOperation(value = "Permite listar todas las imagenes ", notes = "Lista todas las imagenes ",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Lista de todas las imagenes que allán en la base de datos",
            response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al listar las imagenes",
                    response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Error al listar las imagenes, error al servidor",
                    response = ResponseEntity.class)})
    @GetMapping(GET_ALL)
    public ResponseEntity< List<ImageDto> > getAllImages() {
        return new ResponseEntity<>(this.imageBusiness.getAllImages(), HttpStatus.OK);
    }
}
