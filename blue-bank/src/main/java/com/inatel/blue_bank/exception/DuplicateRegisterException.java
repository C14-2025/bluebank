package com.inatel.blue_bank.exception;

public class DuplicateRegisterException extends RuntimeException {
    public DuplicateRegisterException(String message) {
        super(message);
    }
}
