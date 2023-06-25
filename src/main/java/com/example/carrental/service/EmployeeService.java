package com.example.carrental.service;

import com.example.carrental.model.Employee;
import com.example.carrental.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
