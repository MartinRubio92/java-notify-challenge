package com.sondeos.javanotifychallenge.utils;

import com.sondeos.javanotifychallenge.providers.EmailProvider;
import com.sondeos.javanotifychallenge.providers.NotificationProvider;
import com.sondeos.javanotifychallenge.providers.SmsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationProviderFactory {

    private final SmsProvider smsProvider;
    private final EmailProvider emailProvider;

    public NotificationProviderFactory(SmsProvider smsProvider, EmailProvider emailProvider) {
        this.smsProvider = smsProvider;
        this.emailProvider = emailProvider;
    }

    public NotificationProvider getProvider(String type) {
        return switch (type.toLowerCase()) {
            case "sms" -> smsProvider;
            case "email" -> emailProvider;
            default -> throw new IllegalArgumentException("Tipo de notificación no soportado: " + type);
        };
    }
}
