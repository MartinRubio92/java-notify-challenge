package com.sondeos.javanotifychallenge;

import com.sondeos.javanotifychallenge.providers.EmailProvider;
import com.sondeos.javanotifychallenge.providers.SmsProvider;
import com.sondeos.javanotifychallenge.providers.dto.NotifyResultDto;
import com.sondeos.javanotifychallenge.services.NotifyService;
import com.sondeos.javanotifychallenge.services.dto.NotificationProcessResult;
import com.sondeos.javanotifychallenge.utils.NotificationProviderFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class NotifyServiceTests {

    @Autowired
    NotifyService notifyService;

    @MockBean
    NotificationProviderFactory notificationProviderFactory;

    @MockBean
    SmsProvider smsProvider;

    @MockBean
    EmailProvider emailProvider;

    @Test
    void dispatchNotificationRetriesOnFailure() {
        when(notificationProviderFactory.getProvider("email")).thenReturn(emailProvider);

        // Configura el provider para fallar la primera vez y luego tener éxito
        when(emailProvider.notify(anyString(), anyString()))
                .thenThrow(new RuntimeException("Temporary failure"))
                .thenReturn(new NotifyResultDto("sent"));

        Boolean result = notifyService.dispatchNotification("email", "1", "Test message");
        assertTrue(result);  // Verifica que se reintente y eventualmente se envíe
    }

    @Test
    void dispatchNotificationMaxRetriesExceeded() {
        when(notificationProviderFactory.getProvider("sms")).thenReturn(smsProvider);

        // Configura el provider para fallar en todos los intentos
        when(smsProvider.notify(anyString(), anyString()))
                .thenThrow(new RuntimeException("Failure"));

        Boolean result = notifyService.dispatchNotification("sms", "1", "Test message");
        assertFalse(result);  // Verifica que después de varios intentos falle
    }

    @Test
    void processNotifications() {
        NotificationProcessResult result = notifyService.processNotifications();
        System.out.println(result.toString());
        assertEquals(200, result.getProcessed());
        assertTrue(result.getSent() >= 185);
        assertTrue(result.getDuration() < 30);
    }


}
