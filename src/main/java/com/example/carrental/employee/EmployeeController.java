package com.example.carrental.employee;

import com.example.carrental.customer.CustomerDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeDTOMapper employeeDTOMapper;

    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees()
                .stream()
                .map(employeeDTOMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public EmployeeDTO getSingleEmployee(@PathVariable long id) {
        return employeeDTOMapper.mapToDTO(employeeService.getSingleEmployee(id));
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable long id) {
        employeeService.deleteEmployee(id);
    }

    @PutMapping("{id}")
    public EmployeeDTO updateCustomer(@PathVariable long id,
                                      @Valid @RequestBody EmployeeDTO updatedEmployee) {
        return employeeDTOMapper.mapToDTO(employeeService.updateEmployee(id, updatedEmployee));
    }
}
