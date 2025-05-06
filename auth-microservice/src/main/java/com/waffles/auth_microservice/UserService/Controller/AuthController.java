package com.waffles.auth_microservice.UserService.Controller;

import com.waffles.auth_microservice.StandardResponse.RestResponse;
import com.waffles.auth_microservice.UserService.Model.request.LoginCredentials;
import com.waffles.auth_microservice.UserService.Model.request.NewUserRequest;
import com.waffles.auth_microservice.UserService.Model.request.SingpassLogin;
import com.waffles.auth_microservice.UserService.Service.AuthService;
import com.waffles.auth_microservice.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(
            UserService userService,
            AuthService authService
    ) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login/password")
    public ResponseEntity<RestResponse> loginUsingCredentials(@RequestBody LoginCredentials loginCredentials) {

        try {
            return new ResponseEntity<>(
                    new RestResponse().createSuccess(
                            authService.loginUsingCredentials(loginCredentials)
                    ),
                    HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new RestResponse().failure(e.getMessage())
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RestResponse> registerNewUser(@RequestBody NewUserRequest newUserRequest) {
        try {
            return new ResponseEntity<>(
                    new RestResponse().createSuccess(
                            userService.registerNewUser(newUserRequest)
                    ),
                    HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new RestResponse().failure(e.getMessage())
            );
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<RestResponse> validateToken(@RequestParam String token) {
        try {
            return ResponseEntity.ok(
                    new RestResponse().readSuccess(
                            authService.validateToken(token)
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new RestResponse().failure(e.getMessage())
            );
        }
    }

    /// SINGPASS AUTH
    @PostMapping("/login")
    public ResponseEntity<RestResponse> loginUsingSingpass(@RequestBody SingpassLogin singpassLogin) {

        try {
            return new ResponseEntity<>(
                    new RestResponse().createSuccess(
                            authService.loginUsingSingpass(singpassLogin)
                    ),
                    HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new RestResponse().failure(e.getMessage())
            );
        }
    }
}
