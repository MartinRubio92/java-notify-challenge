package com.sondeos.javanotifychallenge.exceptions;

public class MaxRetriesExceededException extends RuntimeException {
    private static final String MESSAGE = "Max retries reached for contact ID ";

    public MaxRetriesExceededException(String contactId) {
        super(MESSAGE + contactId);
    }
}
