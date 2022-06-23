package com.hcmus.lovelybackend.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.hcmus.lovelybackend.exception.runtime.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler({ResourceNotFoundException.class, UserNotFoundException.class, ProductNotFoundException.class,
            NoWinnerYetException.class, NotFoundException.class})
    public ResponseEntity<?> customHandleNotFound(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class, MessagingException.class,
            UnsupportedEncodingException.class, ConfirmPasswordIncorrectException.class,
            HttpMessageNotReadableException.class, AddProductToWatchListException.class,
            JsonParseException.class, SQLIntegrityConstraintViolationException.class
    })
    public ResponseEntity<?> customHandleBadRequest(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RefreshTokenNotValidException.class})
    public ResponseEntity<?> customHandleForbidden(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({CredentialsExpiredException.class, ExpiredJwtException.class, BadCredentialsException.class, IllegalArgumentException.class})
    public ResponseEntity<?> customHandleUnauthorized(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({NoHandlerFoundException.class, SendOTPEmailException.class})
    public ResponseEntity<?> customHandleNotFoundAPI(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({UserAlreadyExsitsException.class})
    public ResponseEntity<?> customHandleUserAlreadyExsitsException(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({VerifyEmailException.class})
    public ResponseEntity<?> customHandleVerifyEmail(Exception ex, HttpServletRequest request) {
        return handleException(ex, request, HttpStatus.EXPECTATION_FAILED);
    }

    public ResponseEntity<?> handleException(Exception ex, HttpServletRequest request, HttpStatus status) {
        CustomExceptionResponse errors = new CustomExceptionResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(status.value());
        errors.setPath(request.getMethod() + " " + request.getRequestURI());
        return new ResponseEntity<>(errors, status);
    }
}
