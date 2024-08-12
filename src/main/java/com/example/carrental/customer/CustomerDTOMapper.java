package com.example.carrental.customer;

import com.example.carrental.user.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDTOMapper {

    private final UserDTOMapper userDTOMapper;

    public CustomerDTO mapToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                userDTOMapper.mapToDTOPost(customer.getUser())
        );
    }
}
