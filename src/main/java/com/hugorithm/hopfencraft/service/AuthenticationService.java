package com.hugorithm.hopfencraft.service;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.hugorithm.hopfencraft.validators.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Role;
import com.hugorithm.hopfencraft.repository.UserRepository;
import com.hugorithm.hopfencraft.dto.LoginResponseDTO;
import com.hugorithm.hopfencraft.repository.RoleRepository;


@Service
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ResponseEntity<?> registerUser(String username, String password, String email){
        try {
            Optional<ApplicationUser> existingUser = userRepository.findByUsername(username);
            Optional<ApplicationUser> existingEmail = userRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                throw new IllegalArgumentException(String.format("Username %s is already taken", username));
            }

            if (existingEmail.isPresent()) {
                throw new IllegalArgumentException(String.format("Email %s is already taken", email));
            }

            if (!EmailValidator.isValid(email)) {
                throw new IllegalArgumentException("Email is not valid");
            }

            String encodedPassword = passwordEncoder.encode(password);
            Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new NoSuchElementException("Role not Found"));

            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            ApplicationUser user = new ApplicationUser();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setAuthorities(authorities);
            user.setEmail(email);

            userRepository.save(user);

            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    public LoginResponseDTO login(String username, String password){

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found")), token);

        } catch(AuthenticationException e){
            throw new UsernameNotFoundException("user not found");
        }
    }

}