package com.example.carrental.repository;

import com.example.carrental.model.Customer;
import com.example.carrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
}
