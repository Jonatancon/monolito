package com.pragma.monolito.domain;

import lombok.*;

@Getter
@Setter
@Builder
public class PersonDto{
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String location;
}
