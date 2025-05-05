package com.waffles.todo_microservice.Feign;

import com.waffles.todo_microservice.StandardResponse.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("AUTH-MICROSERVICE")
public interface AuthMicroservice {

    @GetMapping("/auth/validateToken")
    public ResponseEntity<RestResponse> validateToken(@RequestParam String token);

}
