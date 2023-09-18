package com.hugorithm.hopfencraft.service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.hugorithm.hopfencraft.dto.authentication.LoginDTO;
import com.hugorithm.hopfencraft.dto.authentication.UserRegistrationDTO;
import com.hugorithm.hopfencraft.dto.authentication.UserRegistrationResponseDTO;
import com.hugorithm.hopfencraft.enums.EmailType;
import com.hugorithm.hopfencraft.exception.email.EmailAlreadyTakenException;
import com.hugorithm.hopfencraft.exception.auth.UsernameAlreadyExistsException;
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
import com.hugorithm.hopfencraft.dto.authentication.LoginResponseDTO;
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

    public ResponseEntity<UserRegistrationResponseDTO> registerUser(UserRegistrationDTO body){
        try {
            String username = body.getUsername().toLowerCase();

            Optional<ApplicationUser> existingUser = userRepository.findByUsername(username);

            if (existingUser.isPresent()) {
                throw new UsernameAlreadyExistsException("Username %s is already taken", username);
            }

            Optional<ApplicationUser> existingEmail = userRepository.findByEmail(body.getEmail());

            if (existingEmail.isPresent()) {
                throw new EmailAlreadyTakenException("Email %s is already taken", body.getEmail());
            }

            String encodedPassword = passwordEncoder.encode(body.getPassword());
            Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new RoleNotFoundException("Role not Found"));

            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            ApplicationUser user = new ApplicationUser(
                    username,
                    encodedPassword,
                    body.getEmail(),
                    authorities,
                    body.getFirstName(),
                    body.getLastName(),
                    body.getDateOfBirth(),
                    body.getPhoneNumber()
            );
            userRepository.save(user);

            String message = emailService.buildWelcomeEmail(username);
            String subject = "Welcome to HopfenCraft - Your Registration Was Successful!";

            emailService.sendEmail(user.getEmail(), subject, message, user, EmailType.REGISTRATION);

            UserRegistrationResponseDTO userDto = new UserRegistrationResponseDTO(
                    username,
                    body.getEmail(),
                    body.getFirstName(),
                    body.getLastName(),
                    body.getDateOfBirth(),
                    body.getPhoneNumber()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        } catch (UsernameAlreadyExistsException | EmailAlreadyTakenException | RoleNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    public ResponseEntity<LoginResponseDTO> login(LoginDTO body){
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword()));
            String token = tokenService.generateJwt(auth);
            ApplicationUser user = userRepository.findByUsername(body.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            LoginResponseDTO response = new LoginResponseDTO(user.getUsername(), user.getEmail(), token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex){
            LOGGER.error("Failed to authenticate", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}