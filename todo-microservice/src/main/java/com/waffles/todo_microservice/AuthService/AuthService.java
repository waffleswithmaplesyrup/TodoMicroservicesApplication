package com.waffles.todo_microservice.AuthService;

import com.waffles.todo_microservice.StandardResponse.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthFeignClient authFeignClient;

    @Autowired
    public AuthService(
            AuthFeignClient authFeignClient
    ) {
        this.authFeignClient = authFeignClient;
    }

    public String validateTokenAndReturnUserId(String token) {

        try {
            ResponseEntity<RestResponse> response = authFeignClient.validateTokenAndReturnUserId(token);

            if(response.getBody() == null || response.getBody().getData() == null) throw new RuntimeException("Unable to retrieve data");

            return response.getBody().getData().toString();
        } catch (RuntimeException e) {
            String message = e.getMessage();

            if(message.contains("400")) throw new RuntimeException("Invalid token.");

            if(message.contains("503")) throw new RuntimeException("Auth Microservice is down :(");

            // if token is invalid, return null
            return null;
        }
    }
}
