package com.lazybot.microservices.commons.exceptions;

public class EmptyMapException extends Exception {
    public EmptyMapException(String errorMessage) {
        super(errorMessage);
    }
    public EmptyMapException() {
    }

}
