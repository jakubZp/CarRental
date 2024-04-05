package com.example.carrental.customer;

import com.example.carrental.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from customer c" +
            " left join fetch c.customerRents")
    List<Customer> findAllCustomers();
}
