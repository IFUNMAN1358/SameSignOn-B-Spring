package com.nagornov.multimicroserviceproject.authservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AsyncException extends RuntimeException {
    private final String message;
    private final Throwable cause;
}
