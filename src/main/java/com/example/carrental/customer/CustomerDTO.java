package com.example.carrental.customer;

import com.example.carrental.user.UserDTOPost;
import jakarta.validation.Valid;

public record CustomerDTO(
        Long customerId,
        @Valid UserDTOPost userDTO) {
}
