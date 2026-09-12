package com.vaselek.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "SELLER";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.WORKING;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "dismissal_date")
    private LocalDate dismissalDate;

    @Column(name = "block_date")
    private LocalDate blockDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
    @Override public String getUsername() { return login; }
    @Override public String getPassword() { return password; }
    @Override public boolean isAccountNonExpired() { return status != UserStatus.FIRED; }
    @Override public boolean isAccountNonLocked() { return status != UserStatus.BLOCKED; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return status == UserStatus.WORKING; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public LocalDate getDismissalDate() { return dismissalDate; }
    public void setDismissalDate(LocalDate dismissalDate) { this.dismissalDate = dismissalDate; }
    public LocalDate getBlockDate() { return blockDate; }
    public void setBlockDate(LocalDate blockDate) { this.blockDate = blockDate; }
}
