package com.example.carrental.auth;

import com.example.carrental.config.services.JwtService;
import com.example.carrental.customer.Customer;
import com.example.carrental.employee.Employee;
import com.example.carrental.customer.CustomerRepository;
import com.example.carrental.employee.EmployeeRepository;
import com.example.carrental.user.token.Token;
import com.example.carrental.user.token.TokenRepository;
import com.example.carrental.user.*;
import com.example.carrental.user.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    private User createAndSaveUser(RegisterRequest request, Role role) {
        Optional<User> existingUser = userRepository.findUserByEmail(request.getEmail());
        if(existingUser.isPresent()) {
            throw new IllegalStateException("email is already taken");
        }
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    public AuthenticationResponse registerCustomer(RegisterRequest request) {
        var savedUser = createAndSaveUser(request, Role.CUSTOMER);

        Customer c = new Customer();
        c.setUser(savedUser);
        customerRepository.save(c);

        var jwtToken = jwtService.generateToken(savedUser);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(savedUser.getRole())
                .build();
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public AuthenticationResponse registerEmployee(RegisterRequest request) {
        var savedUser = createAndSaveUser(request, Role.EMPLOYEE);

        Employee e = new Employee();
        e.setUser(savedUser);
        employeeRepository.save(e);

        var jwtToken = jwtService.generateToken(savedUser);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(savedUser.getRole())
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
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
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }
}
