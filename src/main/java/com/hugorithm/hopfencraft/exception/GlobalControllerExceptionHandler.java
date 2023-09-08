package com.hugorithm.hopfencraft.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(PasswordNotValidException.class)
    public ResponseEntity<?> handlePasswordNotValidException(PasswordNotValidException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @ExceptionHandler(UsernameNotValidException.class)
    public ResponseEntity<?> handleUsernameNotValidException(UsernameNotValidException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
