package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class UserService implements UserDetailsService {
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);



    public UserService(JwtService jwtService, TokenService tokenService, EmailService emailService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private LocalDateTime extractDateTimeFromToken(String token) throws IllegalArgumentException {
        String[] parts = token.split("\\|");
        if (parts.length == 2) {
            String dateStr = parts[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            return LocalDateTime.parse(dateStr, formatter);
        }
        throw new IllegalArgumentException("Invalid token format");
    }

    public ResponseEntity<String> sendPasswordResetRequest(Jwt jwt) {
        try {
            ApplicationUser user = jwtService.getUserFromJwt(jwt);
            String token = tokenService.generatePasswordResetToken();
            String decodedToken = tokenService.URLDecodeToken(token);
            LocalDateTime expirationDate = extractDateTimeFromToken(decodedToken);

            user.setPasswordResetTokenExpiration(expirationDate);
            user.setPasswordResetToken(decodedToken);

            userRepository.save(user);

            String subject = "Password Reset";
            String link = emailService.baseUrl + "/user/reset-password?token=" + token;
            String message = emailService.buildEmail(user.getUsername(),"Please use the following link to reset your password", link);

            emailService.sendEmail(user.getEmail(), subject, message);

            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private ApplicationUser verifyPasswordResetToken(Jwt jwt, String token) throws IllegalArgumentException {
        ApplicationUser user = jwtService.getUserFromJwt(jwt);
        LocalDateTime expirationDate = extractDateTimeFromToken(tokenService.URLDecodeToken(token));

        if (!token.equals(user.getPasswordResetToken())) {
            throw new IllegalArgumentException("Invalid token");
        }

        if (expirationDate.equals(user.getPasswordResetTokenExpiration()) && user.getPasswordResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        return user;
    }
    public ResponseEntity<?> showPasswordResetForm(Jwt jwt, String token) {
        try {
            verifyPasswordResetToken(jwt, token);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //TODO: Implement google reCaptcha
    public ResponseEntity<?> resetPassword(Jwt jwt, String token, String oldPassword, String newPassword, String newPasswordConfirmation) {
        try {
            ApplicationUser user = verifyPasswordResetToken(jwt, token);

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new IllegalStateException("Wrong credentials");
            }

            if (oldPassword.equals(newPassword)) {
                throw new IllegalStateException("New password must be different from the old password");
            }

            if (newPassword.equals(newPasswordConfirmation)) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                user.setPasswordResetToken(null);
                user.setPasswordResetTokenExpiration(null);
                userRepository.save(user);

                return ResponseEntity.ok().build();
            } else {
                throw new IllegalStateException("Passwords don't match");
            }

        } catch (IllegalStateException | IllegalArgumentException | UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
