package com.sondeos.javanotifychallenge.exceptions;

public class InvalidEmailFormatException extends RuntimeException {
    public static final String MESSAGE = "Invalid email format for contact ID ";

    public InvalidEmailFormatException(String contactId) {
        super(MESSAGE + contactId);
    }
}
