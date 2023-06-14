package com.example.carrental.service;

import com.example.carrental.model.Customer;
import com.example.carrental.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    public Customer getSingleCustomer(long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    public Customer addCustomer(Customer customer) {
        Optional<Customer> p = customerRepository.findCustomerByEmail(customer.getEmail());
        if(p.isPresent()) {
            throw new IllegalStateException("email is already taken");
        }
        return customerRepository.save(customer);
    }

    public void deleteCustomer(long id) {
        if(!customerRepository.existsById(id)) {
            throw new IllegalStateException("customer with id " + id + " does not exists!");
        }
        customerRepository.deleteById(id);
    }

    @Transactional
    public Customer updateCustomer(long id, Customer updatedCustomer) {
        Customer c = customerRepository.findById(id).orElseThrow(() ->
                    new IllegalStateException("customer with id " + id + " does not exists! Cannot update."));

        //TODO new function isValid to replace "repeated" code? or something similar
        String newFirstName = updatedCustomer.getFirstName();
        if(newFirstName != null && newFirstName.length() > 0 && !Objects.equals(c.getFirstName(), newFirstName)) {
            c.setFirstName(newFirstName);
        }

        String newLastName = updatedCustomer.getLastName();
        if(newLastName != null && newLastName.length() > 0 && !Objects.equals(c.getLastName(), newLastName)) {
            c.setLastName(newLastName);
        }

        String newPhoneNumber = updatedCustomer.getPhoneNumber();
        if(newPhoneNumber != null && newPhoneNumber.length() > 0 && !Objects.equals(c.getPhoneNumber(), newPhoneNumber)) {
            c.setPhoneNumber(newPhoneNumber);
        }

        String newAddress = updatedCustomer.getAddress();
        if(newAddress != null && newAddress.length() > 0 && !Objects.equals(c.getAddress(), newAddress)) {
            c.setAddress(newAddress);
        }

        String newEmail = updatedCustomer.getEmail();
        if(newEmail != null && newEmail.length() > 0 && !Objects.equals(c.getEmail(), newEmail)) {
            Optional<Customer> customerByEmail = customerRepository.findCustomerByEmail(newEmail);
            if(customerByEmail.isPresent()) {
                throw new IllegalStateException("email is already taken");
            }
            c.setEmail(newEmail);
        }

        return c;
    }
}
