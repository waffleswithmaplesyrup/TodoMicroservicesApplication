package com.waffles.todo_microservice.Security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String retrieveToken(HttpServletRequest request) {
        String authorizationToken = request.getHeader("Authorization");

        if(authorizationToken == null || !authorizationToken.startsWith("Bearer")) throw new RuntimeException("Token required");

        return authorizationToken.substring(7);
    }
}
