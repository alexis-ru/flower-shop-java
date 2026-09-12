package com.vaselek.controller;

import com.vaselek.model.User;
import com.vaselek.model.dto;
import com.vaselek.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/director")
public class DirectorController {

    private final UserService userService;

    public DirectorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<dto.UserDTO> listUsers() {
        return userService.findAll();
    }

    @PostMapping("/users")
    public dto.UserDTO createUser(@RequestBody dto.CreateUserRequest req) {
        return userService.create(req);
    }

    @PutMapping("/users/{id}/block")
    public ResponseEntity<?> block(@PathVariable Long id) {
        userService.block(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<?> unblock(@PathVariable Long id) {
        userService.unblock(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> fire(@PathVariable Long id,
                                 @AuthenticationPrincipal User current) {
        try {
            userService.fire(id, current.getId());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
