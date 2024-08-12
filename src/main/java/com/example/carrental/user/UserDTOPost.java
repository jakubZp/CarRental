package com.example.carrental.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserDTOPost(
        Long userId,
        @NotNull(message = "firstName cannot be null")
        @Length(min = 1, message = "firstName length should be greater than 0")
        String firstName,
        @NotNull(message = "lastName cannot be null")
        @Length(min = 1, message = "lastName length should be greater than 0")
        String lastName,
        @NotNull(message = "phoneNumber cannot be null")
        @Length(min = 1, message = "phoneNumber length should be greater than 0")
        String phoneNumber,
        @NotNull(message = "address cannot be null")
        @Length(min = 1, message = "address length should be greater than 0")
        String address,
        @Email(message = "Invalid email format")
        @NotNull(message = "email cannot be null")
        String email
) {
}
