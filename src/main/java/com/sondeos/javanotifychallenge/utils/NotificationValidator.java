package com.sondeos.javanotifychallenge.utils;

import com.sondeos.javanotifychallenge.exceptions.ContactNotFoundException;
import com.sondeos.javanotifychallenge.exceptions.InvalidEmailFormatException;
import com.sondeos.javanotifychallenge.exceptions.InvalidPhoneNumberFormatException;
import com.sondeos.javanotifychallenge.providers.ContactProvider;
import com.sondeos.javanotifychallenge.providers.dto.ContactDto;
import com.sondeos.javanotifychallenge.providers.dto.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Component
public class NotificationValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{10,15}$"
    );

    private final ConcurrentHashMap<String, ContactDto> contactCache = new ConcurrentHashMap<>();

    private final ContactProvider contactProvider;

    public NotificationValidator(ContactProvider contactProvider) {
        this.contactProvider = contactProvider;
    }

    public ValidationResult validateData(String contactId, String type) {
        try {
            // Verificar si el contacto está en caché o cargarlo si no está
            ContactDto contact = contactCache.computeIfAbsent(contactId, contactProvider::getContact);

            return switch (type) {
                case "email" -> validateEmailNotification(contact);
                case "sms" -> validateSmsNotification(contact);
                default -> new ValidationResult(false, null, "Unsupported notification type" + contactId);
            };

        } catch (Exception e) {
            return new ValidationResult(false, null, "Error retrieving contact: " + e.getMessage());
        }
    }

    private ValidationResult validateEmailNotification(ContactDto contact) {
        if (!EMAIL_PATTERN.matcher(contact.getEmail()).matches()) {
            return new ValidationResult(false, null, "Invalid email format for contact ID " + contact.getId());
        }
        return new ValidationResult(true, contact.getEmail(), null);
    }

    private ValidationResult validateSmsNotification(ContactDto contact) {
        if (!PHONE_PATTERN.matcher(contact.getPhoneNumber()).matches()) {
            return new ValidationResult(false, null, "Invalid phone number format for contact ID " + contact.getId());
        }
        return new ValidationResult(true, contact.getPhoneNumber(), null);
    }

}
