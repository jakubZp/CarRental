package com.example.carrental.auth;

import com.example.carrental.config.JwtService;
import com.example.carrental.model.Customer;
import com.example.carrental.model.Employee;
import com.example.carrental.model.Role;
import com.example.carrental.model.User;
import com.example.carrental.repository.CustomerRepository;
import com.example.carrental.repository.EmployeeRepository;
import com.example.carrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public AuthenticationResponse registerCustomer(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                //.role(Role.CUSTOMER) // TODO should RegisterRequest contains Role ??
                .role(request.getRole())
                .build();

        userRepository.save(user);
        if(request.getRole() == Role.CUSTOMER) {
            Customer c = new Customer();
            c.setUser(user);
            customerRepository.save(c);
        }

        if(request.getRole() == Role.EMPLOYEE) {
            Employee e = new Employee();
            e.setUser(user);
            employeeRepository.save(e);
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}