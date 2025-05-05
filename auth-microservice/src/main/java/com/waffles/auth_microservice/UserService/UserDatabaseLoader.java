package com.waffles.auth_microservice.UserService;

import com.waffles.auth_microservice.UserService.Model.User;
import com.waffles.auth_microservice.UserService.Model.request.NewUserRequest;
import com.waffles.auth_microservice.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
public class UserDatabaseLoader implements ApplicationRunner {

    private UserService userService;

    @Autowired
    public UserDatabaseLoader(
            UserService userService
    ) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        NewUserRequest newUser = new NewUserRequest();
        newUser.setEmail("user@email.com");
        newUser.setPassword("p@s5worD");
        newUser.setRole("user");

        userService.registerNewUser(newUser);

        newUser.setEmail("admin@email.com");
        newUser.setPassword("p@s5worD");
        newUser.setRole("admin");

        userService.registerNewUser(newUser);

    }
}
