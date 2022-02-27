package com.eventmanagementsystem.EventManagementSystem.exception;

public class JwtTokenExpiredException extends RuntimeException{
    public JwtTokenExpiredException(String ex)
    {
        super(ex);
    }
}
