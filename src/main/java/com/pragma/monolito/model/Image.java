package com.pragma.monolito.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@Setter
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] imageData;
    @ManyToOne
    private Person person;



    public Integer getImageHashCode(){
        return Arrays.hashCode(this.imageData);
    }
}
