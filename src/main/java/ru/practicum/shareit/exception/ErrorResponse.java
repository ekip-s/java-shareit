package ru.practicum.shareit.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private final String description;

    public ErrorResponse(String description) {
        this.description = "error: " + description;
    }
}
