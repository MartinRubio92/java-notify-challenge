package com.sondeos.javanotifychallenge;

import com.sondeos.javanotifychallenge.exceptions.InvalidEmailFormatException;
import com.sondeos.javanotifychallenge.exceptions.InvalidPhoneNumberFormatException;
import com.sondeos.javanotifychallenge.exceptions.UnsupportedNotificationTypeException;
import com.sondeos.javanotifychallenge.providers.dto.ContactDto;
import com.sondeos.javanotifychallenge.utils.NotificationValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotificationValidatorTests {

    @Autowired
    NotificationValidator notificationValidator;

    @Test
    void validateValidEmail() {
        ContactDto contact = new ContactDto("1", "valid.email@example.com", "1122334455");
        assertDoesNotThrow(() -> notificationValidator.validateData(contact, "email"));
    }

    @Test
    void validateValidPhoneNumber() {
        ContactDto contact = new ContactDto("1", "test@example.com", "1234567890");
        assertDoesNotThrow(() -> notificationValidator.validateData(contact, "sms"));
    }

    @Test
    void validateInvalidEmailFormat() {
        ContactDto contact = new ContactDto("1", "invalid-email", "1122334455");
        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, () ->
                notificationValidator.validateData(contact, "email")
        );
        assertEquals("Invalid email format for contact ID 1", exception.getMessage());
    }

    @Test
    void validateInvalidPhoneNumberFormat() {
        ContactDto contact = new ContactDto("1", "test@example.com", "123");
        InvalidPhoneNumberFormatException exception = assertThrows(InvalidPhoneNumberFormatException.class, () ->
                notificationValidator.validateData(contact, "sms")
        );
        assertEquals("Invalid phone number format for contact ID 1", exception.getMessage());
    }

    @Test
    void validateUnsupportedNotificationType() {
        ContactDto contact = new ContactDto("1", "test@example.com", "1122334455");
        UnsupportedNotificationTypeException exception = assertThrows(UnsupportedNotificationTypeException.class, () ->
                notificationValidator.validateData(contact, "unsupportedType")
        );
        assertEquals("Unsupported notification type for contact ID 1", exception.getMessage());
    }
}
