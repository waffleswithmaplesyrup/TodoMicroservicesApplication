package com.waffles.singpass_simulator.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UUIDController {

    private final List<String> singpassStore = new ArrayList<>(List.of(
            "0f1c9b2f-c155-45d5-804d-a4506e4c054f",
            "64b21d9f-a165-418c-85b7-2afd5e36409e",
            "138949a2-68bb-4f46-998c-1f6e28efd738",
            "199964cf-b7fb-4487-8a7c-0163e6c703d0"
    ));

    @GetMapping("/returnUUID/{id}")
    public ResponseEntity<String> authenticateUsingSingpassAndReturnUserId(@PathVariable long id) {
        try {
            // simulate facial scan and authenticate user
            // for example path variable id is someone's face
            // match the face with the stored uuid
            return ResponseEntity.ok(
                    authenticateUser(id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    e.getMessage()
            );
        }
    }

    private String authenticateUser(long id) {
        if(id >= singpassStore.size()) throw new RuntimeException("Unable to login via Singpass");

        return singpassStore.get((int) id);
    }


}
