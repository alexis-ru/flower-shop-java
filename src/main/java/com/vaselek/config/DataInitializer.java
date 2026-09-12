package com.vaselek.config;

import com.vaselek.model.User;
import com.vaselek.model.UserStatus;
import com.vaselek.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDirector(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByLogin("director").isEmpty()) {
                User director = new User();
                director.setFullName("Директор магазина");
                director.setLogin("director");
                director.setPassword(encoder.encode("director123"));
                director.setRole("DIRECTOR");
                director.setStatus(UserStatus.WORKING);
                director.setRegistrationDate(LocalDate.now());
                repo.save(director);
            }
        };
    }
}
