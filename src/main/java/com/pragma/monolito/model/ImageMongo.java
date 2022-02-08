package com.pragma.monolito.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Arrays;
import java.util.UUID;

@Getter
@Setter
@Document("imagesitems")
public class ImageMongo {
    @Id
    private Long id;
    @Lob
    private byte[] imageData;
    private Long idPerson;

    public ImageMongo(){
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    public Integer getImageHashCode(){
        return Arrays.hashCode(this.imageData);
    }

}
