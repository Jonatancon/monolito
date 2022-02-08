package com.pragma.monolito.repository;

import com.pragma.monolito.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsPersonByEmail(String email);
}
