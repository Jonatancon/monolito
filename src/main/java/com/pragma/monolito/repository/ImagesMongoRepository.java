package com.pragma.monolito.repository;

import com.pragma.monolito.model.ImageMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImagesMongoRepository extends MongoRepository<ImageMongo, Long> {
    void deleteAllByIdPerson(Long idPerson);
}
