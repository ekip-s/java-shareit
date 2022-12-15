package ru.practicum.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private final String error;

    public ErrorResponse(String description) {
        this.error = description;
    }
}