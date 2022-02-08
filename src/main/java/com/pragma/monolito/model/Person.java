package com.pragma.monolito.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String phone;
    private String address;
    private String location;

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name +", lastName=" + lastName + ", email=" + email + ", " +
                "phone=" + phone + " , address=" + address + ", location=" + location + "]";
    }
}
