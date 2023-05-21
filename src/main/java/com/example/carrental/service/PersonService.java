package com.example.carrental.service;

import com.example.carrental.model.Person;
import com.example.carrental.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person getSinglePerson(long id) {
        return personRepository.findById(id).orElseThrow();
    }

    public Person addPerson(Person person) {
        return personRepository.save(person);
    }

    public void deletePerson(long id) {
        personRepository.deleteById(id);
    }
}
