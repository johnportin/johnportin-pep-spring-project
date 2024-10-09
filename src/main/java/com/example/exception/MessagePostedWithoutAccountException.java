package com.example.exception;

public class MessagePostedWithoutAccountException extends RuntimeException {
    public MessagePostedWithoutAccountException(String message) {
        super(message);
    }
}
