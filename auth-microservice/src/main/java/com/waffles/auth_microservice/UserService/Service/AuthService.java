package com.waffles.auth_microservice.UserService.Service;

import com.waffles.auth_microservice.Security.Token.TokenService;
import com.waffles.auth_microservice.UserService.Model.User;
import com.waffles.auth_microservice.UserService.Model.request.LoginCredentials;
import com.waffles.auth_microservice.UserService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authManager;    /// make sure to have this configured in SecurityConfig
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(
            AuthenticationManager authManager,
            TokenService tokenService,
            UserRepository userRepository
    ) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public String loginUsingCredentials(LoginCredentials loginCredentials) {

        if(
                loginCredentials.getEmail() == null || loginCredentials.getEmail().isEmpty() ||
                        loginCredentials.getPassword() == null || loginCredentials.getPassword().isEmpty()
        ) throw new RuntimeException("Login credentials must not be empty.");

        // do a user authentication check
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginCredentials.getEmail(),
                        loginCredentials.getPassword()
                )
        );

        // throw exception if user is not authenticated
        if(!authentication.isAuthenticated()) throw new RuntimeException("Bad Credentials");

        // generate a token
        return tokenService.generateToken(loginCredentials.getEmail());
    }

    /// after validating token, return the userId
    public UUID validateToken(String token) {

        if(token == null || token.isEmpty()) throw new RuntimeException("Token required");

        boolean valid = tokenService.validateToken(token);

        if(!valid) throw new RuntimeException("Token expired");

        String email = tokenService.decodeToken(token);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()) throw new RuntimeException("User not found");

        return userOptional.get().getId();
    }
}
