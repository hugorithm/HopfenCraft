package com.hugorithm.hopfencraft.service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.hugorithm.hopfencraft.dto.UserRegistrationResponseDTO;
import com.hugorithm.hopfencraft.exception.email.EmailAlreadyTakenException;
import com.hugorithm.hopfencraft.exception.auth.UsernameAlreadyExistsException;
import com.hugorithm.hopfencraft.model.Email;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.management.relation.RoleNotFoundException;


@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public ResponseEntity<UserRegistrationResponseDTO> registerUser(String username, String password, String email){
        try {
            username = username.toLowerCase();

            Optional<ApplicationUser> existingUser = userRepository.findByUsername(username);

            if (existingUser.isPresent()) {
                throw new UsernameAlreadyExistsException(String.format("Username %s is already taken", username));
            }

            Optional<ApplicationUser> existingEmail = userRepository.findByEmail(email);

            if (existingEmail.isPresent()) {
                throw new EmailAlreadyTakenException(String.format("Email %s is already taken", email));
            }

            String encodedPassword = passwordEncoder.encode(password);
            Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new RoleNotFoundException("Role not Found"));

            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            ApplicationUser user = new ApplicationUser(username, encodedPassword, email, authorities);
            userRepository.save(user);

            String message = emailService.buildWelcomeEmail(username);
            String subject = "Welcome to HopfenCraft - Your Registration Was Successful!";

            emailService.sendEmail(user.getEmail(), subject, message, user, Email.EmailType.REGISTRATION);

            UserRegistrationResponseDTO userDto = new UserRegistrationResponseDTO(username, email);

            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (UsernameAlreadyExistsException | EmailAlreadyTakenException | RoleNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    public ResponseEntity<LoginResponseDTO> login(String username, String password){
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJwt(auth);
            ApplicationUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            LoginResponseDTO response = new LoginResponseDTO(user.getUsername(), user.getEmail(), token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex){
            LOGGER.error("Failed to authenticate", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}