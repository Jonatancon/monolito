package com.pragma.monolito.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ImageDto {

    private Long id;
    @JsonIgnore
    private byte[] imageData;
    private Long personId;
    private Long hasCode;
}
