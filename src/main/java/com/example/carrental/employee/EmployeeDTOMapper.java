package com.example.carrental.employee;

import com.example.carrental.user.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDTOMapper {

    private final UserDTOMapper userDTOMapper;

    public EmployeeDTO mapToDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                userDTOMapper.mapToDTOPost(employee.getUser()),
                employee.getSalary(),
                employee.getPosition(),
                employee.getEmployedFrom(),
                employee.getEmployedTo()
        );
    }
}
