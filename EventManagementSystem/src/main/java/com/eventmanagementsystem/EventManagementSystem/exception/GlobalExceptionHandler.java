package com.eventmanagementsystem.EventManagementSystem.exception;

import com.eventmanagementsystem.EventManagementSystem.controller.response.ApiResponse;
import com.eventmanagementsystem.EventManagementSystem.enums.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EventIdNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEventIdNotFoundException(EventIdNotFoundException e)
    {
        ApiResponse exceptionResponse = new ApiResponse();
        exceptionResponse.setStatus(Status.FAILED.name());
        exceptionResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailIdAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleEmailIdAlreadyExistException(EmailIdAlreadyExistException e)
    {
        ApiResponse exceptionResponse = new ApiResponse();
        exceptionResponse.setStatus(Status.FAILED.name());
        exceptionResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(JwtTokenExpiredException e)
    {
        ApiResponse exceptionResponse = new ApiResponse();
        exceptionResponse.setStatus(Status.FAILED.name());
        exceptionResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredException.class)
    public ResponseEntity<ApiResponse> handleInvalidCredException(InvalidCredException e)
    {
        ApiResponse exceptionResponse = new ApiResponse();
        exceptionResponse.setStatus(Status.FAILED.name());
        exceptionResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailIdNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEmailIdNotFoundException(EmailIdNotFoundException e)
    {
        ApiResponse exceptionResponse = new ApiResponse();
        exceptionResponse.setStatus(Status.FAILED.name());
        exceptionResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TitleAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleTitleAlreadyExistsException(TitleAlreadyExistsException e)
    {
        ApiResponse exceptionResponse = new ApiResponse();
        exceptionResponse.setStatus(Status.FAILED.name());
        exceptionResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException e)
    {
        ApiResponse exceptionResponse = new ApiResponse();
        exceptionResponse.setStatus(Status.FAILED.name());
        exceptionResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventAlreadyBookedException.class)
    public ResponseEntity<ApiResponse> handleEventAlreadyBookedException(EventAlreadyBookedException e)
    {
        ApiResponse exceptionResponse = new ApiResponse();
        exceptionResponse.setStatus(Status.FAILED.name());
        exceptionResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
