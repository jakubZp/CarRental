package com.example.carrental.controller;

import com.example.carrental.model.Person;
import com.example.carrental.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/persons")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("{id}")
    public Person getSinglePerson(@PathVariable long id) {
        return personService.getSinglePerson(id);
    }

    @PostMapping
    public Person addPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

    @DeleteMapping("{id}")
    public void deletePerson(@PathVariable long id) {
        personService.deletePerson(id);
    }
}
