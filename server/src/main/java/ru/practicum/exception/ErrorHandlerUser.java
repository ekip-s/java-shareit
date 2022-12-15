package ru.practicum.exception;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.validation.ConstraintViolationException;



@RestControllerAdvice("ru.practicum.user")
public class ErrorHandlerUser extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity  handlerIllegalArgumentException(Exception ex) {
        System.out.println("Jib,rf 1");
        ErrorResponse errors = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handlerThrowable(Exception ex) {
        System.out.println("Jib,rf 2");
        ErrorResponse errors = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
    public ResponseEntity handlerDBConflict(Exception ex) {
        System.out.println("Jib,rf 3");
        ErrorResponse errors = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity handlerConflict(Exception ex) {
        System.out.println("Jib,rf 4");
        ErrorResponse errors = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

}