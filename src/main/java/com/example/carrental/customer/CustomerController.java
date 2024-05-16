package com.example.carrental.customer;

import com.example.carrental.customer.Customer;
import com.example.carrental.customer.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerDTOMapper customerDTOMapper;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers()
                .stream()
                .map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public CustomerDTO getSingleCustomer(@PathVariable long id) {
        return customerDTOMapper.apply(customerService.getSingleCustomer(id));
    }

    @PostMapping
    public CustomerDTO addCustomer(@RequestBody Customer customer) {
        return customerDTOMapper.apply(customerService.addCustomer(customer));
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("{id}")
    public CustomerDTO updateCustomer(@PathVariable long id,
                                 @RequestBody Customer updatedCustomer) {
        return customerDTOMapper.apply(customerService.updateCustomer(id, updatedCustomer));
    }
}
