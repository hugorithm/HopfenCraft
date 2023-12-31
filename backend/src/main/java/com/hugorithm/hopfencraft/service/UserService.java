package com.hugorithm.hopfencraft.service;

import com.hugorithm.hopfencraft.dto.authentication.PasswordResetDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetRequestDTO;
import com.hugorithm.hopfencraft.dto.user.PasswordResetResponseDTO;
import com.hugorithm.hopfencraft.enums.EmailType;
import com.hugorithm.hopfencraft.exception.auth.InvalidTokenException;
import com.hugorithm.hopfencraft.exception.auth.PasswordMismatchException;
import com.hugorithm.hopfencraft.exception.auth.SamePasswordException;
import com.hugorithm.hopfencraft.exception.auth.WrongCredentialsException;
import com.hugorithm.hopfencraft.exception.email.EmailSendingFailedException;
import com.hugorithm.hopfencraft.model.ApplicationUser;
import com.hugorithm.hopfencraft.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserDetailsService {
    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Value("${frontend.url}")
    private String FRONTEND_URL;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private LocalDateTime extractDateTimeFromToken(String token) throws InvalidTokenException {
        String[] parts = token.split("\\|");
        if (parts.length == 2) {
            String dateStr = parts[1];
            String[] datePatterns = {
                    "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS",
                    "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSS",
                    "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                    "yyyy-MM-dd'T'HH:mm:ss.SSSSS",
                    "yyyy-MM-dd'T'HH:mm:ss.SSSS",
                    "yyyy-MM-dd'T'HH:mm:ss.SSS",
                    "yyyy-MM-dd'T'HH:mm:ss.SS",
                    "yyyy-MM-dd'T'HH:mm:ss.S",
            };

            for (String pattern : datePatterns) {
                Optional<LocalDateTime> parsedDateTime = tryParseDateTime(dateStr, pattern);
                if (parsedDateTime.isPresent()) {
                    return parsedDateTime.get();
                }
            }

            throw new InvalidTokenException("Unable to parse date-time from token");
        }
        throw new InvalidTokenException("Invalid token format");
    }

    private Optional<LocalDateTime> tryParseDateTime(String input, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return Optional.of(LocalDateTime.parse(input, formatter));
        } catch (DateTimeParseException e) {
            // Parsing failed with this pattern
            return Optional.empty();
        }
    }

    public ResponseEntity<PasswordResetResponseDTO> sendPasswordResetRequest(PasswordResetRequestDTO dto) {
        try {
            ApplicationUser user = userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("Username not found with username %s", dto.getUsername())));

            String token = jwtService.generatePasswordResetToken();
            String decodedToken = jwtService.URLDecodeToken(token);
            LocalDateTime expirationDate = extractDateTimeFromToken(decodedToken);
            user.setPasswordResetTokenExpiration(expirationDate);
            user.setPasswordResetToken(decodedToken);
            userRepository.save(user);

            String link = FRONTEND_URL + "/user/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(user, link);

            return ResponseEntity.ok(new PasswordResetResponseDTO("Password reset email sent successfully"));
        } catch (UsernameNotFoundException | InvalidTokenException | EmailSendingFailedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private ApplicationUser verifyPasswordResetToken(String token) {
        try {
            String decodedToken = jwtService.URLDecodeToken(token);
            ApplicationUser user = userRepository.findByPasswordResetToken(decodedToken)
                    .orElseThrow(() -> new InvalidTokenException("Invalid token"));

            LocalDateTime expirationDate = extractDateTimeFromToken(decodedToken);

            if (expirationDate.equals(user.getPasswordResetTokenExpiration()) && user.getPasswordResetTokenExpiration().isBefore(LocalDateTime.now())) {
                throw new InvalidTokenException("Token has expired");
            }

            return user;
        } catch (InvalidTokenException | UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public ResponseEntity<?> showPasswordResetForm(String token) {
        try {
            verifyPasswordResetToken(token);
            return ResponseEntity.ok().build();
        } catch (InvalidTokenException | UsernameNotFoundException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    public ResponseEntity<?> resetPassword(String token, PasswordResetDTO dto) {
        try {
            ApplicationUser user = verifyPasswordResetToken(token);

            if (dto.getNewPassword().equals(dto.getNewPasswordConfirmation())) {
                String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
                user.setPassword(encodedPassword);
                user.setPasswordResetToken(null);
                user.setPasswordResetTokenExpiration(null);
                userRepository.save(user);

                return ResponseEntity.ok().build();
            } else {
                throw new PasswordMismatchException("Passwords don't match");
            }

        } catch (SamePasswordException | PasswordMismatchException | UsernameNotFoundException | InvalidTokenException |
                 WrongCredentialsException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
