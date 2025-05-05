package com.waffles.auth_microservice.UserService.Model.request;

import lombok.Data;

@Data
public class NewUserRequest {

    private String email;
    private String password;
    private String role;
}
