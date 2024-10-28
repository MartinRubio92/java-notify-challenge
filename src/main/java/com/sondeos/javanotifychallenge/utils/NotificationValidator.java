package com.sondeos.javanotifychallenge.utils;

import com.sondeos.javanotifychallenge.exceptions.InvalidEmailFormatException;
import com.sondeos.javanotifychallenge.exceptions.InvalidPhoneNumberFormatException;
import com.sondeos.javanotifychallenge.exceptions.UnsupportedNotificationTypeException;
import com.sondeos.javanotifychallenge.providers.dto.ContactDto;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NotificationValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{10,15}$"
    );

    public String validateData(ContactDto contact, String type)
            throws InvalidEmailFormatException, InvalidPhoneNumberFormatException, UnsupportedNotificationTypeException {
        return switch (type) {
            case "email" -> validateEmailNotification(contact);
            case "sms" -> validateSmsNotification(contact);
            default -> throw new UnsupportedNotificationTypeException(contact.getId());
        };
    }

    private String validateEmailNotification(ContactDto contact) {
        if (!EMAIL_PATTERN.matcher(contact.getEmail()).matches()) {
            throw new InvalidEmailFormatException(contact.getId());
        }
        return contact.getEmail();
    }

    private String validateSmsNotification(ContactDto contact) {
        if (!PHONE_PATTERN.matcher(contact.getPhoneNumber()).matches()) {
            throw new InvalidPhoneNumberFormatException(contact.getId());
        }
        return contact.getPhoneNumber();
    }

}
