package com.hugorithm.hopfencraft.service;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.hugorithm.hopfencraft.dto.UserRegistrationDTO;
import com.hugorithm.hopfencraft.validators.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ResponseEntity<UserRegistrationDTO> registerUser(String username, String password, String email){
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

            ApplicationUser user = new ApplicationUser(username, encodedPassword, email, authorities);
            userRepository.save(user);

            UserRegistrationDTO userDto = new UserRegistrationDTO(username, email);

            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<LoginResponseDTO> login(String username, String password){
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJwt(auth);
            ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            LoginResponseDTO response = new LoginResponseDTO(user.getUsername(), user.getEmail(), token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex){
            LOGGER.error("Failed to authenticate", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}