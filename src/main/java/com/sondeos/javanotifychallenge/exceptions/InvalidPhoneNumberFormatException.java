package com.sondeos.javanotifychallenge.exceptions;

public class InvalidPhoneNumberFormatException extends RuntimeException {
    public static final String MESSAGE = "Invalid phone number format for contact ID ";

    public InvalidPhoneNumberFormatException(String contactId) {
        super(MESSAGE + contactId);
    }
}
