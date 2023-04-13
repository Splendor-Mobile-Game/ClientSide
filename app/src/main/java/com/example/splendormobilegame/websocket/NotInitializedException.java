package com.example.splendormobilegame.websocket;

public class NotInitializedException extends RuntimeException {
    public NotInitializedException() {

    }

    public NotInitializedException(String message) {
        super(message);
    }

    public NotInitializedException(Throwable cause) {
        super(cause);
    }

    public NotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
