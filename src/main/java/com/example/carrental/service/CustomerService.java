package com.example.carrental.service;

import com.example.carrental.model.Customer;
import com.example.carrental.model.User;
import com.example.carrental.repository.CustomerRepository;
import com.example.carrental.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    @PreAuthorize("#id == authentication.principal.customer.id || hasRole('EMPLOYEE')")
    public Customer getSingleCustomer(long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    public Customer addCustomer(Customer customer) {
        Optional<User> existingUser = userRepository.findUserByEmail(customer.getUser().getEmail());
        if(existingUser.isPresent()) {
            throw new IllegalStateException("email is already taken");
        }
        userRepository.save(customer.getUser()); // TODO do not sure if it should be here - single responsibility
        return customerRepository.save(customer); // user is saved automatically because of Cascade.ALL in relation??? is not
    }

    @PreAuthorize("#id == authentication.principal.customer.id")
    public void deleteCustomer(long id) {
        if(!customerRepository.existsById(id)) {
            throw new IllegalStateException("customer with id " + id + " does not exists!");
        }
        customerRepository.deleteById(id);
    }

    @Transactional
    public Customer updateCustomer(long id, Customer newCustomer) {
        Customer currentCustomer = customerRepository.findById(id).orElseThrow(() ->
                    new IllegalStateException("customer with id " + id + " does not exists! Cannot update."));

        User updatedUser = newCustomer.getUser();
        User user = currentCustomer.getUser();

        String newFirstName = updatedUser.getFirstName();
        if(newFirstName != null && newFirstName.length() > 0 && !Objects.equals(user.getFirstName(), newFirstName)) {
            user.setFirstName(newFirstName);
        }

        String newLastName = updatedUser.getLastName();
        if(newLastName != null && newLastName.length() > 0 && !Objects.equals(user.getLastName(), newLastName)) {
            user.setLastName(newLastName);
        }

        String newPhoneNumber = updatedUser.getPhoneNumber();
        if(newPhoneNumber != null && newPhoneNumber.length() > 0 && !Objects.equals(user.getPhoneNumber(), newPhoneNumber)) {
            user.setPhoneNumber(newPhoneNumber);
        }

        String newAddress = updatedUser.getAddress();
        if(newAddress != null && newAddress.length() > 0 && !Objects.equals(user.getAddress(), newAddress)) {
            user.setAddress(newAddress);
        }

        String newEmail = updatedUser.getEmail();
        if(newEmail != null && newEmail.length() > 0 && !Objects.equals(user.getEmail(), newEmail)) {
            Optional<User> userByEmail = userRepository.findUserByEmail(newEmail);
            if(userByEmail.isPresent()) {
                throw new IllegalStateException("email is already taken");
            }
            user.setEmail(newEmail);
        }

        return currentCustomer;
    }
}
