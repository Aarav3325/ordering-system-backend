package com.aarav.orderservice.controller;

import com.aarav.orderservice.security.GoogleTokenVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/test")
public class AuthTestController {

    private final GoogleTokenVerifier googleTokenVerifier;

    public AuthTestController(GoogleTokenVerifier googleTokenVerifier) {
        this.googleTokenVerifier = googleTokenVerifier;
    }

    @PostMapping("/google")
    public ResponseEntity<?> testGoogle(@RequestBody Map<String, String> body) {
        String token = body.get("idToken");

        var payload =googleTokenVerifier.verify(token);

        return ResponseEntity.ok(
                Map.of(
                        "email", payload.getEmail(),
                        "name", payload.get("name")
                )
        );
    }
}
