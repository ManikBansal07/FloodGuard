package com.floodguard.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, Map<String, String> errors, long timestamp) {
        super(status, message, timestamp);
        this.errors = errors;
    }
} 