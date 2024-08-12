package com.example.carrental.user;

import jakarta.validation.constraints.NotNull;

public record UserDTOGet(
        Long userId,
        @NotNull(message = "firstName cannot be null")
        String firstName,
        @NotNull(message = "lastName cannot be null")
        String lastName,
        @NotNull(message = "phoneNumber cannot be null")
        String phoneNumber,
        @NotNull(message = "address cannot be null")
        String address,
        @NotNull(message = "email cannot be null")
        String email,
        Role role,
        UserStatus status
) {
}
