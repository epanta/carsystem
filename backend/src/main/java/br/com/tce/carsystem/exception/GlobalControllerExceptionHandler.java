package br.com.tce.carsystem.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConversion(ConversionFailedException ex) {
        final var errors = new HashMap<String, String>();
        errors.put("message", "Invalid fields");
        errors.put("errorCode", String.valueOf(ex.getValue()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put("message", "Invalid fields");
            errors.put("errorCode", String.valueOf(ex.getStatusCode().value()));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(SQLIntegrityConstraintViolationException ex) {
        final var errors = new HashMap<String, String>();
        errors.put("message", ex.getMessage());
        errors.put("errorCode", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMessageNotReadableExceptions(HttpMessageNotReadableException ex) {
        final var errors = new HashMap<String, String>();
        errors.put("message", "Invalid fields");
        errors.put("errorCode", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageConversionxceptions(HttpMessageConversionException ex) {
        final var errors = new HashMap<String, String>();
        errors.put("message", "Invalid fields");
        errors.put("errorCode", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        final var errors = new HashMap<String, String>();
        errors.put("message", "Invalid fields");
        errors.put("errorCode", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(InternalErrorException ex) {
        final var errors = new HashMap<String, String>();
        errors.put("message", ex.getMessage());
        errors.put("errorCode", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
