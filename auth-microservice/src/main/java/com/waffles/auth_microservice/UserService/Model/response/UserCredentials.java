package com.waffles.auth_microservice.UserService.Model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {

    private String email;
    private String nric;
    private String uuid;
}
