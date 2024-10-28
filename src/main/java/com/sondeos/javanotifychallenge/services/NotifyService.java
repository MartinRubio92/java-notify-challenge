package com.sondeos.javanotifychallenge.services;

import com.sondeos.javanotifychallenge.providers.NotificationProvider;
import com.sondeos.javanotifychallenge.providers.dto.NotifyResultDto;
import com.sondeos.javanotifychallenge.providers.dto.ValidationResult;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/* Esta clase y sus métodos deben ser modificados para cumplir el challenge */

@Service
public class NotifyService {

    private static final Logger logger = LoggerFactory.getLogger(NotifyService.class);

    private final NotificationProviderFactory notificationProviderFactory;
    private final NotificationValidator notificationValidator;
    private final Executor notificationExecutor;

    @Autowired
    public NotifyService(
            NotificationProviderFactory notificationProviderFactory,
            NotificationValidator notificationValidator,
            @Qualifier("notificationExecutor") Executor notificationExecutor
    ) {
        this.notificationProviderFactory = notificationProviderFactory;
        this.notificationValidator = notificationValidator;
        this.notificationExecutor = notificationExecutor;
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

        // Lista para almacenar mensajes de error
        List<String> errorLogs = new ArrayList<>();

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
        // Intentamos validar los datos, y si falla, lanzará una excepción que el ExceptionHandler gestionará
        ValidationResult validationResult = notificationValidator.validateData(contactId, type);
        if (!validationResult.isValid()) {
            System.out.println("Validation failed: " + validationResult.getErrorMessage());
            return false;
        }
        try {
            return RetryHandler.executeWithRetry(() -> {
                NotificationProvider provider = notificationProviderFactory.getProvider(type);
                NotifyResultDto result = provider.notify(validationResult.getContactData(), message);
                return "sent".equalsIgnoreCase(result.getStatus());
            }, 3, 1000);
        } catch (Exception e) {
            System.out.println("Error sending notification after retries: " + e.getMessage());
            return false;
        }
    }
}
