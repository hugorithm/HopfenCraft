package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.exception.auth.InvalidTokenException;
import com.hugorithm.hopfencraft.exception.auth.PasswordMismatchException;
import com.hugorithm.hopfencraft.exception.auth.SamePasswordException;
import com.hugorithm.hopfencraft.exception.auth.WrongCredentialsException;
import com.hugorithm.hopfencraft.exception.email.EmailSendingFailedException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.model.Email;
import com.hugorithm.hopfencraft.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserDetailsService {
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private LocalDateTime extractDateTimeFromToken(String token) throws InvalidTokenException {
        String[] parts = token.split("\\|");
        if (parts.length == 2) {
            String dateStr = parts[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            return LocalDateTime.parse(dateStr, formatter);
        }
        throw new InvalidTokenException("Invalid token format");
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
            String message = emailService.buildPasswordResetEmail(user.getUsername(), link);

            emailService.sendEmail(user.getEmail(), subject, message, user, Email.EmailType.PASSWORD_RESET);

            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (UsernameNotFoundException | InvalidTokenException | EmailSendingFailedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private ApplicationUser verifyPasswordResetToken(Jwt jwt, String token) throws InvalidTokenException {
        ApplicationUser user = jwtService.getUserFromJwt(jwt);
        String decodedToken = tokenService.URLDecodeToken(token);
        LocalDateTime expirationDate = extractDateTimeFromToken(decodedToken);

        if (!decodedToken.equals(user.getPasswordResetToken())) {
            throw new InvalidTokenException("Invalid token");
        }

        if (expirationDate.equals(user.getPasswordResetTokenExpiration()) && user.getPasswordResetTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token has expired");
        }

        return user;
    }
    public ResponseEntity<?> showPasswordResetForm(Jwt jwt, String token) {
        try {
            verifyPasswordResetToken(jwt, token);
            return ResponseEntity.ok().build();
        } catch (InvalidTokenException | UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //TODO: Implement google reCaptcha
    public ResponseEntity<?> resetPassword(Jwt jwt, String token, String oldPassword, String newPassword, String newPasswordConfirmation) {
        try {
            ApplicationUser user = verifyPasswordResetToken(jwt, token);

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new WrongCredentialsException("Wrong credentials");
            }

            if (oldPassword.equals(newPassword)) {
                throw new SamePasswordException("New password must be different from the old password");
            }

            if (newPassword.equals(newPasswordConfirmation)) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encodedPassword);
                user.setPasswordResetToken(null);
                user.setPasswordResetTokenExpiration(null);
                userRepository.save(user);

                return ResponseEntity.ok().build();
            } else {
                throw new PasswordMismatchException("Passwords don't match");
            }

        } catch (SamePasswordException | PasswordMismatchException | UsernameNotFoundException | InvalidTokenException | WrongCredentialsException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
