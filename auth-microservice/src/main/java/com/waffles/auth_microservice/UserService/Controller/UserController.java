package com.waffles.auth_microservice.UserService.Controller;

import com.waffles.auth_microservice.StandardResponse.RestResponse;
import com.waffles.auth_microservice.UserService.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    ///  only logged in user can see logged in user profile
    @GetMapping("/viewProfile")
    public ResponseEntity<RestResponse> viewProfile(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(
                    new RestResponse().readSuccess(
                            userService.viewProfile(request)
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new RestResponse().failure(e.getMessage())
            );
        }
    }

}
