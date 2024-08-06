package com.example.carrental.customer;

import jakarta.validation.Valid;
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
                .map(customerDTOMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public CustomerDTO getSingleCustomer(@PathVariable long id) {
        return customerDTOMapper.mapToDTO(customerService.getSingleCustomer(id));
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable long id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("{id}")
    public CustomerDTO updateCustomer(@PathVariable long id,
                                 @Valid @RequestBody CustomerDTO updatedCustomer) {
        return customerDTOMapper.mapToDTO(customerService.updateCustomer(id, updatedCustomer));
    }
}
