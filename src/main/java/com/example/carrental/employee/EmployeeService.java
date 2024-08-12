package com.example.carrental.employee;

import com.example.carrental.customer.Customer;
import com.example.carrental.customer.CustomerDTO;
import com.example.carrental.employee.Employee;
import com.example.carrental.employee.EmployeeRepository;
import com.example.carrental.user.User;
import com.example.carrental.user.UserDTOPost;
import com.example.carrental.user.UserService;
import com.example.carrental.user.UserStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Employee getSingleEmployee(long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("employee with id " + id + " does not exists!")
        );
    }

    @PreAuthorize("(hasAuthority('ADMIN')) || (#id == authentication.principal.customer.id)")
    @Transactional
    public void deleteEmployee(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("employee with id " + id + " does not exists!"));
        employee.getUser().setStatus(UserStatus.DELETED);
        employeeRepository.save(employee);
    }

    @PreAuthorize("(hasAuthority('ADMIN')) || (#id == authentication.principal.customer.id)")
    @Transactional
    public Employee updateEmployee(long id, EmployeeDTO newEmployee) {
        Employee currentEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("employee with id " + id + " does not exists! Cannot update."));

        User currentUser = currentEmployee.getUser();
        UserDTOPost newUser = newEmployee.userDTO();

        userService.updateUserFields(currentUser, newUser);
        currentEmployee.setSalary(newEmployee.salary());
        currentEmployee.setPosition(newEmployee.position());
        currentEmployee.setEmployedFrom(newEmployee.employedFrom());
        currentEmployee.setEmployedTo(newEmployee.employedTo());

        return currentEmployee;
    }
}
