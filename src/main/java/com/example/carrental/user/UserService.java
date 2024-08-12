package com.example.carrental.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void updateUserFields(User currentUser, UserDTOPost newUserData) {
        currentUser.setFirstName(newUserData.firstName());
        currentUser.setLastName(newUserData.lastName());
        currentUser.setPhoneNumber(newUserData.phoneNumber());
        currentUser.setAddress(newUserData.address());
        currentUser.setEmail(updateEmail(currentUser, newUserData.email()));
    }

    private String updateEmail(User currentUser, String newEmail) {
        if (!Objects.equals(currentUser.getEmail(), newEmail)) {
            if (userRepository.findUserByEmail(newEmail).isPresent()) {
                throw new IllegalStateException("email is already taken");
            }
            return newEmail;
        }
        return currentUser.getEmail();
    }
}
