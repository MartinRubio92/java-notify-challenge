package com.sondeos.javanotifychallenge.services;

import com.sondeos.javanotifychallenge.exceptions.InvalidEmailFormatException;
import com.sondeos.javanotifychallenge.exceptions.InvalidPhoneNumberFormatException;
import com.sondeos.javanotifychallenge.exceptions.MaxRetriesExceededException;
import com.sondeos.javanotifychallenge.exceptions.UnsupportedNotificationTypeException;
import com.sondeos.javanotifychallenge.providers.ContactProvider;
import com.sondeos.javanotifychallenge.providers.NotificationProvider;
import com.sondeos.javanotifychallenge.providers.dto.ContactDto;
import com.sondeos.javanotifychallenge.providers.dto.NotifyResultDto;
import com.sondeos.javanotifychallenge.repository.NotificationRepository;
import com.sondeos.javanotifychallenge.services.dto.NotificationProcessResult;
import com.sondeos.javanotifychallenge.utils.NotificationProviderFactory;
import com.sondeos.javanotifychallenge.utils.NotificationValidator;
import com.sondeos.javanotifychallenge.utils.RetryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/* Esta clase y sus métodos deben ser modificados para cumplir el challenge */

@Service
public class NotifyService {

    private static final Logger logger = LoggerFactory.getLogger(NotifyService.class);

    private final NotificationProviderFactory notificationProviderFactory;
    private final NotificationValidator notificationValidator;
    private final Executor notificationExecutor;

    private final ConcurrentHashMap<String, ContactDto> contactCache = new ConcurrentHashMap<>();
    private final ContactProvider contactProvider;

    @Autowired
    public NotifyService(
            NotificationProviderFactory notificationProviderFactory,
            NotificationValidator notificationValidator,
            @Qualifier("notificationExecutor") Executor notificationExecutor,
            ContactProvider contactProvider
    ) {
        this.notificationProviderFactory = notificationProviderFactory;
        this.notificationValidator = notificationValidator;
        this.notificationExecutor = notificationExecutor;
        this.contactProvider = contactProvider;
    }

    /*
    * Procesa todas las notificaciones y devuelve un objeto con el número de notificaciones procesadas, enviadas y el tiempo de procesamiento
    */
    public NotificationProcessResult processNotifications(){
        //Iniciamos contador de tiempo
        long startTime = System.currentTimeMillis();

        //Iniciamos contador de notificaciones procesadas
        AtomicInteger processed = new AtomicInteger();

        //Iniciamos contador de notificaciones enviadas
        AtomicInteger sent = new AtomicInteger();

        // Usamos el Executor inyectado
        CompletableFuture.allOf(NotificationRepository.getNotifications().stream()
                .map(n -> CompletableFuture.supplyAsync(() -> {
                    Boolean result = this.dispatchNotification(n.get("type"), n.get("contactId"), n.get("message"));
                    processed.incrementAndGet();
                    if (result) {
                        sent.incrementAndGet();
                    }
                    return result;
                }, notificationExecutor)).toArray(CompletableFuture[]::new)).join();

        //Calculamos el tiempo de procesamiento
        long duration = (System.currentTimeMillis() - startTime) / 1000;

        //Devolvemos el resultado con el número de notificaciones procesadas y enviadas y el tiempo de procesamiento
        return new NotificationProcessResult(processed.get(), sent.get(), duration)  ;
    }

    /**
     * Procesa cada notificación e intenta enviarla.
     * Utiliza el Factory para obtener el proveedor correcto.
     *
     * @return true si la notificación se envió correctamente, false en caso contrario.
     */
    public Boolean dispatchNotification(String type, String contactId, String message){
        try {
            // Verificar si el contacto está en caché o cargarlo si no está
            ContactDto contact = contactCache.computeIfAbsent(contactId, contactProvider::getContact);
            String contactData = notificationValidator.validateData(contact, type);

            return RetryHandler.executeWithRetry(contactId,
                () -> {
                    NotificationProvider provider = notificationProviderFactory.getProvider(type);
                    NotifyResultDto result = provider.notify(contactData, message);
                    return "sent".equalsIgnoreCase(result.getStatus());
            }, 3, 1000);

        } catch (MaxRetriesExceededException |
                 InvalidEmailFormatException |
                 UnsupportedNotificationTypeException |
                 InvalidPhoneNumberFormatException e) {
            logger.error(e.getMessage());
            return false;
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Contact not found for contact ID {}", contactId);
            return false;
        } catch (HttpClientErrorException e) {
            logger.error("Error retrieving contact for contact ID {}", contactId);
            return false;
        }
    }
}
