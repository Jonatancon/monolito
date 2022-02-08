package com.pragma.monolito.rest;

import com.pragma.monolito.business.ImageBusiness;
import com.pragma.monolito.business.PersonBusiness;
import com.pragma.monolito.domain.PersonDto;
import com.pragma.monolito.domain.Response;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PersonRest.PERSON)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PersonRest {

    public static final String PERSON = "/api/v1/person";
    public static final String SAVE = "/save";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String GET_PERSON = "/get-person";
    public static final String GET_ALL = "/get-all";

    private final PersonBusiness personBusiness;
    private final ImageBusiness imageBusiness;

    @Autowired
    public PersonRest(@Qualifier("personBusinessImpl") PersonBusiness personBusiness,
                      @Qualifier("imagesMongoImpl")ImageBusiness imageBusiness) {
        this.personBusiness = personBusiness;
        this.imageBusiness = imageBusiness;
    }

    @ApiOperation(value = "Permite crear una nueva persona en el sistema", notes = "Crea un nuevo registro en la base" +
            " de datos sobre la tabla PERSON, con base al JSON recibido", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiParam(name = "person", value = "JSON con la infomacion del producto", required = true, type = "PersonDto")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "La persona fue creada correctamente",
            response = ResponseEntity.class),
    @ApiResponse(code = 400, message = "Error a la hora de crear la persona, debido a los datos recibidos",
            response = ResponseEntity.class),
    @ApiResponse(code = 400, message = "Error a la hora de crear la persona, debido al email",
            response = ResponseEntity.class),
    @ApiResponse(code = 500, message = "Error en la creación de la persona, por parte interno del servidor",
            response = ResponseEntity.class)})
    @PostMapping(SAVE)
    public ResponseEntity<Response> savePerson(@RequestBody PersonDto personDto){
        Response res = this.personBusiness.createPerson(personDto);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @ApiOperation(value = "Permite actualizar la información del sistema",
            notes = "Actualiza el registro de la base de datos, sobre la tabla PERSON, con base al JSON recibido",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiParam(name = "person", value = "JSON con la infomacion del producto", required = true, type = "PersonDto")
    @ApiResponses(value = { @ApiResponse(code = 202, message = "La persona fue actualizada correctamente",
            response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error a la hora de actualizar la persona, debido a los datos recibidos",
                    response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error a la hora de actualizar la persona, debido al email",
                    response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Error en la actualización de la persona, por parte interno del servidor",
                    response = ResponseEntity.class)})
    @PostMapping(UPDATE)
    public ResponseEntity<Response> updatePerson(@RequestBody PersonDto personDto) {
        Response res = this.personBusiness.updatePerson(personDto);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @ApiOperation(value = "Permite eliminar un registro del sistema",
            notes = "Elimina un registro, sobre la tabla PERSON, con base al id enviado",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiParam(name = "id", value = "id de la persona a eliminar", required = true, type = "Long")
    @ApiResponses(value = { @ApiResponse(code = 202, message = "La persona fue eliminada del sistema",
            response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al tratar de eliminar la persona, no existe ese id",
                    response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Error al eliminar la persona, por parte interno del servidor",
                    response = ResponseEntity.class)})
    @DeleteMapping(DELETE)
    public ResponseEntity<Response> deletePerson(@RequestParam Long id){
        imageBusiness.deleteAllImagesOfPerson(id);
        Response res = this.personBusiness.deletePerson(id);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus()));
    }

    @ApiOperation(value = "Permite consultar todos los registro que allán en el sistema",
            notes = "Trae todos los registros que allán en la tabla PERSON",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "lista de las personas en la base de datos",
            response =  ResponseEntity.class),
            @ApiResponse(code = 500, message = "Error al tratar de traer los registros",
                    response = ResponseEntity.class)})
    @GetMapping(GET_ALL)
    public ResponseEntity< List<PersonDto> > getAllPerson(){
        return new ResponseEntity<>(this.personBusiness.getAllPerson(), HttpStatus.OK);
    }

    @ApiOperation(value = "Permite Consultar un registro en la base de datos",
            notes = "Trae el registro de la tabla PERSON, con base al id enviado",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiParam(name = "id", value = "id de la persona a consultar", required = true, type = "Long")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Json de la persona",
            response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Error al tratar de conseguir la información de la persona, no existe ese id",
                    response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "Error al consultar en la base de datos, por parte interno del servidor",
                    response = ResponseEntity.class)})
    @GetMapping(GET_PERSON)
    public ResponseEntity<PersonDto> getPerson(@RequestParam Long id){
        return new ResponseEntity<>(this.personBusiness.findPerson(id), HttpStatus.OK);
    }

}
