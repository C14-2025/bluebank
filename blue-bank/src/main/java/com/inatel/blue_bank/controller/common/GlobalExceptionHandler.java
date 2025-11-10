package com.inatel.blue_bank.controller.common;

import com.inatel.blue_bank.exception.CustomerNotFoundException;
import com.inatel.blue_bank.exception.DeniedOperationException;
import com.inatel.blue_bank.exception.DuplicateRegisterException;
import com.inatel.blue_bank.model.dto.ErrorField;
import com.inatel.blue_bank.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorField> errorsList = e.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName;
                    if (error instanceof FieldError fe) {
                        fieldName = fe.getField();
                    } else {
                        // Case @ValidPhoneNumber (class-level)
                        fieldName = error.getObjectName();
                    }
                    return new ErrorField(fieldName, error.getDefaultMessage());
                })
                .collect(Collectors.toList());

        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error",
                errorsList
        );
    }

    @ExceptionHandler(DuplicateRegisterException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateRegisterException(DuplicateRegisterException e) {
        return ErrorResponse.conflict(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnhandledException(RuntimeException e){
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please contact administration.",
                List.of()
        );
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomerNotFoundException(CustomerNotFoundException e) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of()
        );
    }

    @ExceptionHandler(DeniedOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomerNotFoundException(DeniedOperationException e) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), e.getMessage(), List.of()
        );
    }
}
