package com.waffles.auth_microservice.UserService.Model.request;

import lombok.Data;

@Data
public class LoginCredentials {

    private String email;
    private String password;
}
