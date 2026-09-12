package com.vaselek.controller;

import com.vaselek.model.User;
import com.vaselek.model.dto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;

    public AuthController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody dto.LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.login(), req.password()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            User user = (User) auth.getPrincipal();
            return ResponseEntity.ok(Map.of(
                    "role", user.getRole(),
                    "fullName", user.getFullName(),
                    "login", user.getLogin()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Неверный логин или пароль"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current")
    public ResponseEntity<?> current(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "role", user.getRole(),
                "fullName", user.getFullName(),
                "login", user.getLogin()
        ));
    }
}
