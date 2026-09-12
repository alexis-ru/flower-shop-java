package com.vaselek.model;

import java.time.LocalDate;

public class dto {

    public record UserDTO(Long id, String fullName, String login, String role,
                          String status, LocalDate registrationDate,
                          LocalDate dismissalDate, LocalDate blockDate) {
        public static UserDTO from(User u) {
            return new UserDTO(u.getId(), u.getFullName(), u.getLogin(), u.getRole(),
                    u.getStatus().name(), u.getRegistrationDate(),
                    u.getDismissalDate(), u.getBlockDate());
        }
    }

    public record CreateUserRequest(String fullName, String login, String password, String role) {}

    public record FlowerDTO(Long id, String name, Integer quantity,
                            LocalDate arrivalDate, LocalDate saleDate,
                            Long sellerId, String sellerName) {
        public static FlowerDTO from(Flower f) {
            return new FlowerDTO(f.getId(), f.getName(), f.getQuantity(),
                    f.getArrivalDate(), f.getSaleDate(),
                    f.getSeller().getId(), f.getSeller().getFullName());
        }
    }

    public record CreateFlowerRequest(String name, Integer quantity, LocalDate arrivalDate) {}

    public record UpdateFlowerRequest(Integer quantity, LocalDate saleDate) {}

    public record LoginRequest(String login, String password) {}
}
