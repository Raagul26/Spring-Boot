package com.eventmanagementsystem.EventManagementSystem.exception;

public class EventAlreadyBookedException extends RuntimeException{
    public EventAlreadyBookedException(String ex)
    {
        super(ex);
    }
}
