package com.sondeos.javanotifychallenge.providers.dto;

public class ValidationResult {
    private final boolean valid;
    private final String contactData;
    private final String errorMessage;

    public ValidationResult(boolean valid, String contactData, String errorMessage) {
        this.valid = valid;
        this.contactData = contactData;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return valid;
    }

    public String getContactData() {
        return contactData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

