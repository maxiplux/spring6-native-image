package app.quantun.api.controller;

import app.quantun.api.errors.ErrorMessage;
import app.quantun.api.exceptions.InvalidTokenException;
import app.quantun.api.models.dtos.AuthenticationTokenDto;
import app.quantun.api.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage globalExceptionHandlerAccessDenied(Exception ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage globalExceptionHandlerBadCredentialsException(Exception ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

    @ExceptionHandler({InvalidTokenException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage globalExceptionHandlerBadRequest(Exception ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }


}
