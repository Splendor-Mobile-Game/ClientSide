package com.example.splendormobilegame.config.exceptions;

public class InvalidConfigException extends Exception {

    public InvalidConfigException() {
    }

    public InvalidConfigException(String message) {
        super(message);
    }

    public InvalidConfigException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}