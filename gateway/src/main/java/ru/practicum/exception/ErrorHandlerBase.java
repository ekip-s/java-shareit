package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice()
public class ErrorHandlerBase {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerArgumentNotValid(final MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        String error = String.format("Unknown state: %s", ex.getValue());
        return new ErrorResponse(error);
    }
}