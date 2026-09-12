package com.vaselek.service;

import com.vaselek.model.User;
import com.vaselek.model.UserStatus;
import com.vaselek.model.dto;
import com.vaselek.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public List<dto.UserDTO> findAll() {
        return repo.findAll().stream().map(dto.UserDTO::from).toList();
    }

    public dto.UserDTO create(dto.CreateUserRequest req) {
        User u = new User();
        u.setFullName(req.fullName());
        u.setLogin(req.login());
        u.setPassword(encoder.encode(req.password()));
        u.setRole(req.role() != null ? req.role() : "SELLER");
        u.setStatus(UserStatus.WORKING);
        u.setRegistrationDate(LocalDate.now());
        return dto.UserDTO.from(repo.save(u));
    }

    public void block(Long id) {
        repo.findById(id).ifPresent(u -> {
            u.setStatus(UserStatus.BLOCKED);
            u.setBlockDate(LocalDate.now());
            repo.save(u);
        });
    }

    public void unblock(Long id) {
        repo.findById(id).ifPresent(u -> {
            u.setStatus(UserStatus.WORKING);
            u.setBlockDate(null);
            repo.save(u);
        });
    }

    public void fire(Long id, Long currentUserId) {
        if (id.equals(currentUserId))
            throw new IllegalArgumentException("Нельзя уволить самого себя");
        repo.findById(id).ifPresent(u -> {
            u.setStatus(UserStatus.FIRED);
            u.setDismissalDate(LocalDate.now());
            repo.save(u);
        });
    }

    public User findByLogin(String login) {
        return repo.findByLogin(login).orElse(null);
    }
}
