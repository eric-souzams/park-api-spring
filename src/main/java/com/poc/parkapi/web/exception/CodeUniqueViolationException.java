package com.poc.parkapi.web.exception;

public class CodeUniqueViolationException extends RuntimeException {

    public CodeUniqueViolationException(String message) {
        super(message);
    }
}
