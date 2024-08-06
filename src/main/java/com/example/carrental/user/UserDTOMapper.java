package com.example.carrental.user;

import org.springframework.stereotype.Service;

@Service
public class UserDTOMapper {

    public UserDTOGet mapToDTOGet(User user) {
        return new UserDTOGet(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }

    public UserDTOPost mapToDTOPost(User user) {
        return new UserDTOPost(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getEmail()
        );
    }
}
