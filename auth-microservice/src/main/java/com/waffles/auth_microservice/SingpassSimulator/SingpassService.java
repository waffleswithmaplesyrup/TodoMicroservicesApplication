package com.waffles.auth_microservice.SingpassSimulator;

import com.waffles.auth_microservice.UserService.Model.request.SingpassLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SingpassService {

    private final SingpassFeignClient singpassFeignClient;

    @Autowired
    public SingpassService(
            SingpassFeignClient singpassFeignClient
    ) {
        this.singpassFeignClient = singpassFeignClient;
    }

    public String authenticateUsingSingpassAndReturnUserId(SingpassLogin singpassLogin) {

        ResponseEntity<String> response = singpassFeignClient.authenticateUsingSingpassAndReturnUserId(singpassLogin.getFacialScan());

        return response.getBody();
    }
}
