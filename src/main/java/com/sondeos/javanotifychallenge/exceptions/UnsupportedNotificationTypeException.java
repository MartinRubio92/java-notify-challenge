package com.sondeos.javanotifychallenge.exceptions;

public class UnsupportedNotificationTypeException extends RuntimeException {
    public static final String MESSAGE = "Unsupported notification type for contact ID ";

    public UnsupportedNotificationTypeException(String contactId) {
        super(MESSAGE + contactId);
    }
}
