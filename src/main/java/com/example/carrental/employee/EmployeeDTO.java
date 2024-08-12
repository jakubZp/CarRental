package com.example.carrental.employee;

import com.example.carrental.user.UserDTOPost;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EmployeeDTO(
        Long employeeId,
        @Valid UserDTOPost userDTO,
        @NotNull(message = "salary cannot be null")
        @Positive(message = "salary must be positive")
        BigDecimal salary,
        @NotNull(message = "position cannot be null")
        String position,
        @NotNull(message = "employedFrom cannot be null")
        LocalDateTime employedFrom,
        LocalDateTime employedTo
) {
}
