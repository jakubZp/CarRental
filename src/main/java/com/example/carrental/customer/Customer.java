package com.example.carrental.customer;

import com.example.carrental.user.User;
import com.example.carrental.rental.Rental;
import com.example.carrental.user.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customer")
    public List<Rental> customerRents;
}
