package com.example.carrental.employee;

import com.example.carrental.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal salary;
    private String position;
    private LocalDateTime employedFrom;
    @Column(name = "employed_to")
    private LocalDateTime employedTo;
}
