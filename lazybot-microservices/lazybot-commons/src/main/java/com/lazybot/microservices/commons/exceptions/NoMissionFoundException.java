package com.lazybot.microservices.commons.exceptions;

public class NoMissionFoundException extends Exception {
    public NoMissionFoundException(String errorMessage) {
        super(errorMessage);
    }
    public NoMissionFoundException() {
    }
}
