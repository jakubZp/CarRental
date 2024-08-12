package com.example.carrental.customer;

import com.example.carrental.user.User;
import com.example.carrental.user.UserDTOPost;
import com.example.carrental.user.UserService;
import com.example.carrental.user.UserStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Customer getSingleCustomer(long id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("customer with id " + id + " does not exists!")
        );
    }

    @PreAuthorize("(hasAuthority('EMPLOYEE')) || (#id == authentication.principal.customer.id)")
    @Transactional
    public void deleteCustomer(long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
          () -> new IllegalStateException("customer with id " + id + " does not exists!"));
        customer.getUser().setStatus(UserStatus.DELETED);
        customerRepository.save(customer);
    }

    @PreAuthorize("(hasAuthority('EMPLOYEE')) || (#id == authentication.principal.customer.id)")
    @Transactional
    public Customer updateCustomer(long id, CustomerDTO newCustomer) {
        Customer currentCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("customer with id " + id + " does not exists! Cannot update."));

        User currentUser = currentCustomer.getUser();
        UserDTOPost newUser = newCustomer.userDTO();

        userService.updateUserFields(currentUser, newUser);

        return currentCustomer;
    }
}
