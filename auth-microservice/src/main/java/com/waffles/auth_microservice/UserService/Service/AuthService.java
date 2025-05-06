package com.waffles.auth_microservice.UserService.Service;

import com.waffles.auth_microservice.Security.Token.TokenService;
import com.waffles.auth_microservice.SingpassSimulator.SingpassService;
import com.waffles.auth_microservice.UserService.Enum.Role;
import com.waffles.auth_microservice.UserService.Model.User;
import com.waffles.auth_microservice.UserService.Model.request.LoginCredentials;
import com.waffles.auth_microservice.UserService.Model.request.SingpassLogin;
import com.waffles.auth_microservice.UserService.Model.response.UserCredentials;
import com.waffles.auth_microservice.UserService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    private final List<UserCredentials> militaryStore = new ArrayList<>(List.of(
            new UserCredentials("lalo@salamanca.com", "S1234567W", "64b21d9f-a165-418c-85b7-2afd5e36409e"),
            new UserCredentials("jesse@pinkman.com", "S7654321P", "138949a2-68bb-4f46-998c-1f6e28efd738"),
            new UserCredentials("saul@good.man", "S1928375G", "199964cf-b7fb-4487-8a7c-0163e6c703d0")
    ));

    private final List<String> singpassStore = new ArrayList<>(List.of(
            "0f1c9b2f-c155-45d5-804d-a4506e4c054f",
            "64b21d9f-a165-418c-85b7-2afd5e36409e",
            "138949a2-68bb-4f46-998c-1f6e28efd738",
            "199964cf-b7fb-4487-8a7c-0163e6c703d0"
    ));

    private final AuthenticationManager authManager;    /// make sure to have this configured in SecurityConfig
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final SingpassService singpassService;

    @Autowired
    public AuthService(
            AuthenticationManager authManager,
            TokenService tokenService,
            UserRepository userRepository,
            SingpassService singpassService
    ) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.singpassService = singpassService;
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

    /// SINGPASS
    public String loginUsingSingpass(SingpassLogin singpassLogin) {

        /// call Singpass API to authenticate user and return user uuid
        UUID uuid = singpassAuthentication(singpassLogin);

        if(uuid == null || uuid.toString().isEmpty()) throw new RuntimeException("Unable to log in using Singpass");

        /// call Military datastore passing the user uuid to return user info like name and email
        UserCredentials userCredentials = getUserCredentialsFromMilitaryStore(uuid);

        String email = userCredentials.getEmail();
        String nric = userCredentials.getNric();

        // after user successfully log in via Singpass
        // check if the user is in our db
        Optional<User> userOptional = userRepository.findByEmail(email);
        // TODO: if uuid returned from Singpass is unique to each user, can we find user by uuid?
        //       Optional<User> userOptional = userRepository.findById(uuid);

        if(userOptional.isEmpty()) {
            // user doesn't exist in our db
            // save their credentials into db
            userRepository.save(new User(uuid, email, nric, Role.USER));
            // their nric is their password for now
        }

        /// at this point, user is already authenticated and can now access endpoints

        // generate a token
        return tokenService.generateToken(email);
    }

    private UserCredentials getUserCredentialsFromMilitaryStore(UUID uuid) {
        /// call Military datastore passing the user uuid to return user info like name and email
//        Optional<UserCredentials> userCredentialsOptional = militaryDbSimulator.findUser(uuid);
        Optional<UserCredentials> userCredentialsOptional = militaryStore.stream().filter(user -> Objects.equals(user.getUuid(), uuid.toString())).findFirst();
        // TODO: make sure to catch exceptions
        if(userCredentialsOptional.isEmpty()) throw new RuntimeException("No user found");

        return userCredentialsOptional.get();
    }

    private UUID singpassAuthentication(SingpassLogin singpassLogin) {
        /// call Singpass API to authenticate user and return user uuid
        try {
            String uuid = singpassService.authenticateUsingSingpassAndReturnUserId(singpassLogin);
            return UUID.fromString(uuid);
        } catch (RuntimeException e) {
            if(e.getMessage().contains("503")) throw new RuntimeException("Singpass service down :(");

            return null;
        }
    }
}
