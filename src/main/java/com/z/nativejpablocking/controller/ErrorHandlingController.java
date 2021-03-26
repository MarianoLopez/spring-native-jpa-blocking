package com.z.nativejpablocking.controller;

import com.z.nativejpablocking.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.from(exception));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.from(exception));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> argumentNotValidHandler(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .message("MethodArgumentNotValidException")
                        .payload(groupByError(exception))
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> constraintHandler(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .message("ConstraintViolationException")
                        .payload(groupByError(exception))
                        .build()
        );
    }

    private Map<String, List<FieldError>> groupByError(ConstraintViolationException exception) {
        return exception
                .getConstraintViolations()
                .stream()
                .map(this::toFieldError)
                .collect(Collectors.groupingBy(FieldError::getField));
    }

    private FieldError toFieldError(ConstraintViolation<?> it) {
        return new FieldError(it.getRootBeanClass().getSimpleName(), it.getPropertyPath().toString(), it.getMessage());
    }

    private Map<String, List<FieldError>> groupByError(MethodArgumentNotValidException exception) {
        return exception
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField));
    }
}