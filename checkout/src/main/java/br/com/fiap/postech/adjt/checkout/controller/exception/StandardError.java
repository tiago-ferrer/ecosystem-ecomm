package br.com.fiap.postech.adjt.checkout.controller.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record StandardError(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
    public static StandardError create(HttpStatus status, String message, String path) {
        return new StandardError(
                Instant.now(),
                status.value(),
                status.name(),
                message,
                path
        );
    }
}
