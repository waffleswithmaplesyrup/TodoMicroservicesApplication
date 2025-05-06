package com.waffles.auth_microservice.SingpassSimulator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("SINGPASS-SIMULATOR")
public interface SingpassFeignClient {

    @GetMapping("/returnUUID/{id}")
    public ResponseEntity<String> authenticateUsingSingpassAndReturnUserId(@PathVariable long id);
}
