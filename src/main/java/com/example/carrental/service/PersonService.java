package com.example.carrental.service;

import com.example.carrental.model.Person;
import com.example.carrental.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return personRepository.findAllPersons();
    }

    public Person getSinglePerson(long id) {
        return personRepository.findById(id).orElseThrow();
    }

    public Person addPerson(Person person) {
        Optional<Person> p = personRepository.findPersonByEmail(person.getEmail());
        if(p.isPresent()) {
            throw new IllegalStateException("email is already taken");
        }
        return personRepository.save(person);
    }

    public void deletePerson(long id) {
        if(!personRepository.existsById(id)) {
            throw new IllegalStateException("person with id " + id + " does not exists!");
        }
        personRepository.deleteById(id);
    }

    @Transactional
    public Person updatePerson(long id, Person updatedPerson) {
        Person p = personRepository.findById(id).orElseThrow(() ->
                    new IllegalStateException("person with id " + id + " does not exists! Cannot update."));

        //TODO new function isValid to replace "repeated" code? or something similar
        String newFirstName = updatedPerson.getFirstName();
        if(newFirstName != null && newFirstName.length() > 0 && !Objects.equals(p.getFirstName(), newFirstName)) {
            p.setFirstName(newFirstName);
        }

        String newLastName = updatedPerson.getLastName();
        if(newLastName != null && newLastName.length() > 0 && !Objects.equals(p.getLastName(), newLastName)) {
            p.setLastName(newLastName);
        }

        String newPhoneNumber = updatedPerson.getPhoneNumber();
        if(newPhoneNumber != null && newPhoneNumber.length() > 0 && !Objects.equals(p.getPhoneNumber(), newPhoneNumber)) {
            p.setPhoneNumber(newPhoneNumber);
        }

        String newAddress = updatedPerson.getAddress();
        if(newAddress != null && newAddress.length() > 0 && !Objects.equals(p.getAddress(), newAddress)) {
            p.setAddress(newAddress);
        }

        String newEmail = updatedPerson.getEmail();
        if(newEmail != null && newEmail.length() > 0 && !Objects.equals(p.getEmail(), newEmail)) {
            Optional<Person> personByEmail = personRepository.findPersonByEmail(newEmail);
            if(personByEmail.isPresent()) {
                throw new IllegalStateException("email is already taken");
            }
            p.setEmail(newEmail);
        }

        return p;
    }
}
