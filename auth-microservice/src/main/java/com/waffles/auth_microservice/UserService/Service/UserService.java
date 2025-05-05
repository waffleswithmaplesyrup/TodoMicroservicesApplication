package com.waffles.auth_microservice.UserService.Service;

import com.waffles.auth_microservice.Security.Token.TokenService;
import com.waffles.auth_microservice.UserService.Enum.Role;
import com.waffles.auth_microservice.UserService.Model.User;
import com.waffles.auth_microservice.UserService.Model.request.NewUserRequest;
import com.waffles.auth_microservice.UserService.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public User registerNewUser(NewUserRequest newUser) {
        if(
                newUser.getEmail() == null || newUser.getEmail().isEmpty() ||
                        newUser.getPassword() == null || newUser.getPassword().isEmpty() ||
                        newUser.getRole() == null || newUser.getRole().isEmpty()
        ) throw new RuntimeException("User credentials must not be empty.");

        if(!validateEmail(newUser.getEmail())) throw new RuntimeException("Invalid email address");

        if(!validatePassword(newUser.getPassword())) throw new RuntimeException("Password needs to at be least 8 characters long. Must include lowercase and uppercase characters, at least one digit and one special character and should not include white space.");

        Optional<User> userOptional = userRepository.findByEmail(newUser.getEmail());
        if(userOptional.isPresent()) throw new RuntimeException("The email address " + newUser.getEmail() + " is already taken.");

        return userRepository.save(userRequestToUserMapper(newUser));
    }

    private User userRequestToUserMapper(NewUserRequest newUser) {
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setEmail(newUser.getEmail());
        user.setRole(Role.valueOf(newUser.getRole().toUpperCase()));

        return user;
    }

    private boolean validatePassword(String password) {
        // check to see if the password given has all the required characters
        String regexPattern = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        return Pattern.compile(regexPattern)
                .matcher(password)
                .matches();
    }

    private boolean validateEmail(String email) {
        // check to see if the email given is a valid email address
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public User viewProfile(HttpServletRequest request) {
        String token = tokenService.retrieveToken(request);

        String email = tokenService.decodeToken(token);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()) throw new RuntimeException("User not found");

        return userOptional.get();
    }
}
