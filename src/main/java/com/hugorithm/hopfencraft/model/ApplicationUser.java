package com.hugorithm.hopfencraft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hugorithm.hopfencraft.enums.RegistrationSource;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Data
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    @Email
    private String email;
    private String firstName;
    private String lastName;
    @Column(name = "password_reset_token")
    private String passwordResetToken;
    @Column(name = "password_reset_token_expiration")
    private LocalDateTime passwordResetTokenExpiration;
    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
    @Column(name = "registration_source")
    @Enumerated(EnumType.STRING)
    private RegistrationSource registrationSource;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_junction",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )

    private Set<Role> authorities;
    
    public ApplicationUser() {
        super();
        this.authorities = new HashSet<>();
    }

    public ApplicationUser(String username, String password, String email, Set<Role> authorities, String firstName, String lastName, RegistrationSource registrationSource) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationSource = registrationSource;
        this.cartItems = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
