package com.inatel.blue_bank.exception;

public class DeniedOperationException extends RuntimeException {
    public DeniedOperationException(String message) {
        super(message);
    }
}
