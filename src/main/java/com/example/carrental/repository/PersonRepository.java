package com.example.carrental.repository;

import com.example.carrental.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findPersonByEmail(String email);

    @Query("select p from Person p" +
            " left join fetch p.personRents")
    List<Person> findAllPersons();
}
