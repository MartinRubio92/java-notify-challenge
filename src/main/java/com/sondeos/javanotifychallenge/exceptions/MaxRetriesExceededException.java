package com.sondeos.javanotifychallenge.exceptions;

public class MaxRetriesExceededException extends RuntimeException {
    public MaxRetriesExceededException(String message) {
        super(message);
    }
}
