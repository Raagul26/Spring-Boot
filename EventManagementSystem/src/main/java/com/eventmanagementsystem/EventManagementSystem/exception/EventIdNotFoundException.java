package com.eventmanagementsystem.EventManagementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EventIdNotFoundException extends RuntimeException{
    public EventIdNotFoundException(String exception)
    {
        super(exception);
    }
}
